package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.BlogEntry;
import model.ErrorMessage;
import model.User;
import service.BlogService;
import service.BlogServiceImpl;
import service.UserService;
import service.UserServiceImpl;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ApiServlet", value = "/domain/blog.php")
public class ApiServlet extends HttpServlet {

    private final ObjectMapper mapper = new ObjectMapper();
    private final UserService userService = new UserServiceImpl();
    private final BlogService blogService = new BlogServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        String action = request.getParameter("action");
        if(action != null){
            switch (action){
                case "login":
                    try {
                        User user = userService.login(request.getParameter("user"), request.getParameter("password"));
                        request.getSession().setAttribute("user", user);
                    }catch (LoginException e) {
                        response.getWriter().append(mapper.writeValueAsString(new ErrorMessage(e.getMessage())));
                    }
                    break;
                case "new":
                    BlogEntry entry = new BlogEntry();
                    entry.setText(request.getParameter("text"));
                    String result = blogService.createEntry(entry, (User) request.getSession().getAttribute("user"));
                    if(result != null) response.getWriter().append(mapper.writeValueAsString(new ErrorMessage(result)));
                    break;
                case "new_user":
                    User user = new User();
                    user.setUserName(request.getParameter("user"));
                    user.setPassword(request.getParameter("password"));
                    user.setPermission(request.getParameter("permission"));
                    user.setReadOnly(request.getParameter("readonly"));
                    result = userService.createUser(user);
                    if(result != null) response.getWriter().append(mapper.writeValueAsString(new ErrorMessage(result)));
                    break;
                case "delete":
                    try {
                        result = blogService.deleteEntry(Integer.parseInt(request.getParameter("id")), (User) request.getSession().getAttribute("user"));
                        if (result != null)
                            response.getWriter().append(mapper.writeValueAsString(new ErrorMessage(result)));
                    }catch (NumberFormatException e){
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().append(mapper.writeValueAsString(new ErrorMessage("Wrong id format")));
                    }
                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().append(mapper.writeValueAsString(new ErrorMessage("Unknown action")));
            }
        }else {
            response.getWriter().append(mapper.writeValueAsString(blogService.getAll()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
