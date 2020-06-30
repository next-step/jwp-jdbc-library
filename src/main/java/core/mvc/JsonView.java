package core.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class JsonView implements View {
    private final String contentType = "application/json";
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(contentType);
        response.setCharacterEncoding("UTF-8");
        String jsonValue = StringUtils.EMPTY;

        for(String key : model.keySet()) {
            jsonValue = jsonValue.concat(objectMapper.writeValueAsString(model.get(key)));
        }

        response.getWriter().write(jsonValue);
        response.setContentLength(jsonValue.length());
    }
}
