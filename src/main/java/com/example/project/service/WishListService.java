package com.example.project.service;

import com.example.project.dto.WishListDetailDto;
import com.example.project.dto.WishListItemDto;
import com.example.project.entity.Item;
import com.example.project.entity.Member;
import com.example.project.entity.WishList;
import com.example.project.entity.WishListItem;
import com.example.project.repository.ItemRepository;
import com.example.project.repository.MemberRepository;
import com.example.project.repository.WishListItemRepository;
import com.example.project.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WishListService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final WishListRepository wishListRepository;
    private final WishListItemRepository wishListItemRepository;

    public Long addWishList(WishListItemDto wishListItemDto, String userId){

        Item item = itemRepository.findById(wishListItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByUserId(userId).get();

        WishList wishList = wishListRepository.findByMemberId(member.getId());
        if(wishList == null){
            wishList = WishList.createCart(member);
            wishListRepository.save(wishList);
        }
        WishListItem savedWishListItem = wishListItemRepository.findByWishListIdAndItemId(wishList.getId(), item.getId());

        if(savedWishListItem != null){
            savedWishListItem.addCount(wishListItemDto.getCount());
            return savedWishListItem.getId();
        } else {
            WishListItem wishListItem = WishListItem.createWishListItem(wishList, item, wishListItemDto.getCount());
            wishListItemRepository.save(wishListItem);
            return wishListItem.getId();
        }
    }

    // 장바구니에 들어있는 상품 조회
    @Transactional(readOnly = true)
    public List<WishListDetailDto> getWishList(String userId){

        List<WishListDetailDto> wishListDetailDtoList = new ArrayList<>();
        Member member = memberRepository.findByUserId(userId).get();
        WishList wishList = wishListRepository.findByMemberId(member.getId());
        if(wishList == null){
            return wishListDetailDtoList;
        }
        wishListDetailDtoList = wishListItemRepository.findWishListDetailDtoList(wishList.getId());
        return wishListDetailDtoList;
    }

    public void deleteWishListItem(Long wishListItemId) {
        WishListItem wishListItem = wishListItemRepository.findById(wishListItemId).orElseThrow(EntityNotFoundException::new);
        wishListItemRepository.delete(wishListItem);
    }

    // 위시리스트 상품의 수량 업데이트
    @Transactional(readOnly = true)
    public boolean validateWishListItem(Long wishListItemId, String userId){
        Member curMember = memberRepository.findByUserId(userId).get();
        WishListItem wishListItem = wishListItemRepository.findById(wishListItemId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = wishListItem.getWishList().getMember();

        if(!StringUtils.equals(curMember.getUserId(), savedMember.getUserId())){
            return false;
        }
        return true;
    }
}