package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public Member findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member;
    }

    @GetMapping("/members2/{id}")
    public Member findMember2(@PathVariable("id") Member member) {
        return member;
    }

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("userA"));
    }
}
