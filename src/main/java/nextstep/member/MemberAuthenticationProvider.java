package nextstep.member;

import auth.AuthenticationProvider;
import auth.TokenRequest;
import auth.UserDetails;
import lombok.RequiredArgsConstructor;
import nextstep.exceptions.exception.LoginFailException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAuthenticationProvider implements AuthenticationProvider {

    private final MemberDao memberDao;

    @Override
    public UserDetails getUserDetails(TokenRequest tokenRequest){
        Member member = memberDao.findByUsername(tokenRequest.getUsername())
                .orElseThrow(LoginFailException::new);
        if (member == null || !member.doesPasswordMatch(tokenRequest.getPassword())) {
            throw new LoginFailException();
        }
        return new UserDetails(member.getId(), member.getRole());
    }
}
