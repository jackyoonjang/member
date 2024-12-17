package com.codingrecipe.member.service;

import com.codingrecipe.member.dto.MemberDTO;
import com.codingrecipe.member.entity.MemberEntity;
import com.codingrecipe.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toMemberEntity(memberDTO));
    }

    public MemberDTO login(MemberDTO memberDTO) {
        // 조회
        Optional<MemberEntity> byEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if (byEmail.isPresent()) { // 조회 결과가 있을 경우
            MemberEntity memberEntity = byEmail.get();
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                // 비번 일치
                // 조회 정보 저장
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            }else { // 비번 불일치
                return null;
            }
        }else { // 조회 결과가 없을 경우
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntityList) {
            MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
            memberDTOList.add(dto);
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> byId = memberRepository.findById(id);
        if (byId.isPresent()) {
            return MemberDTO.toMemberDTO(byId.get());
        }else{
            return null;
        }
    }

    public MemberDTO findByEmail(String loginEmail) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(loginEmail);
        if (byMemberEmail.isPresent()) {
            return MemberDTO.toMemberDTO(byMemberEmail.get());
        }else {
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        // id값이 DB에 있다면, update 쿼리를 날린다.
        // 때문에 받아온 memberDTO의 id값을 직접 다시 Entity로 지정해야 한다.
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    public String emailCheck(String memberEmail) {
        Optional<MemberEntity> byEmail = memberRepository.findByMemberEmail(memberEmail);
        if (byEmail.isPresent()) {
            return null;
        }else{
            return "ok";
        }
    }
}
