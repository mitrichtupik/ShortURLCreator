package mitrich.rest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

@WebFilter(filterName = "authFilter", urlPatterns = { "/url/", "/urls/user/*" })
public class AuthFilter implements Filter {

	@Autowired
	AuthenticationService authenticationService;

	public static final String AUTHENTICATION_HEADER = "Authorization";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String authCredentials = httpServletRequest.getHeader(AUTHENTICATION_HEADER);

			// better injected

			// boolean authenticationStatus =
			// authenticationService.authenticate(authCredentials);
			boolean authenticationStatus = "Bearer 123456789abcdef".equals(authCredentials);

			if (authenticationStatus) {
				System.out.println("authFilter");
				chain.doFilter(request, response);
			} else {
				if (response instanceof HttpServletResponse) {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
							"This user is forbidden to create short URL");
				}
			}
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}
