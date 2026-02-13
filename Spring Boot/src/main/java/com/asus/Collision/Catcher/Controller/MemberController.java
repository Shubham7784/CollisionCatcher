package com.asus.Collision.Catcher.Controller;

import com.asus.Collision.Catcher.Entity.Member;
import com.asus.Collision.Catcher.Entity.User;
import com.asus.Collision.Catcher.Service.MemberService;
import com.asus.Collision.Catcher.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllMembers()
    {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User byName = userService.findByName(userName);
        ArrayList<Member> members = byName.getMembers();
        if(!members.isEmpty())
        {
            return new ResponseEntity<>(members, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<?> addMember(@RequestBody Member member)
    {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User byName = userService.findByName(userName);
        memberService.saveMember(member);
        ArrayList<Member> members = byName.getMembers();
        members.add(member);
        byName.setMembers(members);
        userService.saveUser(byName);
        return  new ResponseEntity<>(member,HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateMember(@RequestBody Member member)
    {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User byName = userService.findByName(userName);
        List<Member> list = byName.getMembers().stream().filter(x -> x.getMemberId().equals(member.getMemberId())).toList();
        if(!list.isEmpty())
        {
            Member memberDb = list.getFirst();
            memberDb.setName((member.getName()!=null && !member.getName().isEmpty())?member.getName():memberDb.getName());
            memberDb.setPhoneNo((member.getPhoneNo()!=null && !member.getPhoneNo().isEmpty())?member.getPhoneNo():memberDb.getPhoneNo());
            memberDb.setRelation((member.getRelation()!=null && !member.getRelation().isEmpty())?member.getRelation():memberDb.getRelation());
            memberService.saveMember(memberDb);
            byName.getMembers().removeIf(x->x.getMemberId().equals(memberDb.getMemberId()));
            list.add(memberDb);
            userService.saveUser(byName);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    public  ResponseEntity<?> deletedMember(@RequestBody Member member)
    {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User userDb = userService.findByName(userName);
        userDb.getMembers().removeIf(x -> x.getName().equals(member.getName()));
        boolean deleted = memberService.deleteByName(member.getName());
        if(deleted) {
            userService.saveUser(userDb);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
