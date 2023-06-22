package com.example.project.service;

import com.example.project.dto.ItemDto;
import com.example.project.dto.ItemImgDto;
import com.example.project.entity.Category;
import com.example.project.entity.Item;
import com.example.project.entity.ItemImg;
import com.example.project.repository.CategoryRepository;
import com.example.project.repository.ItemImgRepository;
import com.example.project.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgRepository itemImgRepository;
    private final ItemImgService itemImgService;
    private final CategoryRepository categoryRepository;

    /**  상품 조회 */
    @Transactional(readOnly = true)
    public Page<Item> allItemView(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }




    /**  상품 등록 */
    public Long saveItem(ItemDto itemDto, List<MultipartFile> itemImgFileList) throws Exception{
         //상품 등록
         Item item = itemDto.createItem();
         itemRepository.save(item);

         //이미지 등록
         for(int i=0;i<itemImgFileList.size();i++){
             ItemImg itemImg = new ItemImg();
             itemImg.setItem(item);

             if(i == 0)
                 itemImg.setRepImgYn("Y");
             else
                 itemImg.setRepImgYn("N");

             itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
         }
        return item.getId();
    }

    /**  상품 상세보기  */
    @Transactional
    public ItemDto findById(Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new IllegalArgumentException("글 상세 보기 실패 : 해당 글을 찾을 수 없음"));
        return ItemDto.toItemDto(item);
    }

    @Transactional(readOnly = true)
    public ItemDto getItemDtl(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.toItemImgDto(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        ItemDto itemFormDto = ItemDto.toItemDto(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    /**   목록,   검색 + 페이징   */
    public Page<ItemDto> getItemList(String searchKeyword, Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        Pageable pageRequest = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        Page<Item> items;
        List<ItemDto> itemDtoList = new ArrayList<>();

        if (searchKeyword == null) {
            items = itemRepository.findAll(pageRequest);
        } else {
            items = itemRepository.findByItemNameContaining(searchKeyword, pageRequest);
        }

        for (Item item : items) {
            ItemDto itemDto = new ItemDto();
            ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(item.getId(), "Y");
            itemDtoList.add(ItemDto.toItemListDto(item, itemImg.getSavePath()));
        }

        return new PageImpl<>(itemDtoList, pageable, items.getTotalElements());
    }

    /**   카테고리     */
    public Page<ItemDto> getItemListByCategory(String categoryName, String searchKeyword, Pageable pageable) {

        int page = pageable.getPageNumber() - 1;
        Pageable pageRequest = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());

        Optional<Category> optionalCategory = categoryRepository.findByName(categoryName);

        if (optionalCategory.isEmpty()) {
            return new PageImpl<>(Collections.emptyList());
        }

        Category category = optionalCategory.get();
        Page<Item> items;
        List<ItemDto> itemDtoList = new ArrayList<>();

        if (searchKeyword == null) {
            items = itemRepository.findByCategoryId(pageRequest, category.getId());
        } else {
            items = itemRepository.findByCategoryIdAndItemNameContaining(category.getId(), searchKeyword, pageRequest);
        }

        for (Item item : items) {
            ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(item.getId(), "Y");
            itemDtoList.add(ItemDto.toItemListDto(item, itemImg.getSavePath()));
        }
        return new PageImpl<>(itemDtoList, pageable, items.getTotalElements());
    }

    /**  상품 수정  */
    public Long updateItem(Long itemId, ItemDto itemDto, List<MultipartFile> itemImgFileList) throws Exception {
        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemDto);
        List<Long> itemImgIds = itemDto.getItemImgIds();

        for (int i = 0; i < itemImgFileList.size(); i++) {
            if (itemImgIds.size() > i) {
                itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
            } else {
                itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
            }
        }
        return item.getId();
    }

    /**  상품 삭제 */
    public void delete(Long id){
        itemRepository.deleteById(id);
    }

}