package gift;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    private final MemberService memberService;
    private final JwtService jwtService;

    public MemberController(MemberService memberService, JwtService jwtService) {
        this.memberService = memberService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody Member member) {
        try {
            Member savedMember = memberService.createMember(member);
            Map<String, String> response = new HashMap<>();
            response.put("token", jwtService.generateToken(savedMember));
            logger.debug("Register - Generated Token: " + response.get("token"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Error during registration: " + e.getMessage(), e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody Member member) {
        Member foundMember = getValidMember(member);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtService.generateToken(foundMember));
        logger.debug("Login - Generated Token: " + response.get("token"));
        return ResponseEntity.ok(response);
    }

    private Member getValidMember(Member member) {
        Optional<Member> foundMemberOpt = memberService.getMemberByEmail(member.getEmail());
        if (foundMemberOpt.isEmpty() || !isPasswordValid(foundMemberOpt.get(), member.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return foundMemberOpt.get();
    }

    private boolean isPasswordValid(Member foundMember, String password) {
        return foundMember.getPassword().equals(password);
    }

    @GetMapping("/profile")
    public ResponseEntity<Member> getProfile(@LoginMember Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member not found or unauthorized");
        }
        return ResponseEntity.ok(member);
    }
}
