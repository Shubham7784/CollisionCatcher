package com.asus.Collision.Catcher.Service;

import com.asus.Collision.Catcher.Entity.Member;
import com.asus.Collision.Catcher.Repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public void saveMember(Member member)
    {
        memberRepository.save(member);
    }

    public boolean deleteByName(String memberName)
    {
        Member byName = memberRepository.findByname(memberName);
        memberRepository.delete(byName);
        return true;
    }

    public Member findByName(String memberName)
    {
        return memberRepository.findByname(memberName);
    }
}
