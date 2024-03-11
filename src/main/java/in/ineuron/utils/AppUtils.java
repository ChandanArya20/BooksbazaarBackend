package in.ineuron.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AppUtils {

    @Autowired
    private HttpServletRequest request;

    public String getBaseURL(){
        // Get the base URL dynamically from the current request
        return request.getRequestURL().toString().replace(request.getRequestURI(), "");
    }

}
