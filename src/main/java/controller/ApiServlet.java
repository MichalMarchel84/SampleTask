package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import service.UserService;
import service.UserServiceImpl;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        if(action != null){
            switch (action){
                case "login":
                    String result = userService.login(request.getParameter("user"), request.getParameter("password"));
                    if(result == null) request.getSession(true);
                    else response.getWriter().append(result);
                    break;
                case "new":

                    break;
                case "new_user":
                    User user = new User();
                    user.setUserName(request.getParameter("user"));
                    user.setPassword(request.getParameter("password"));
                    user.setPermission(request.getParameter("permission"));
                    user.setReadOnly(request.getParameter("readonly"));
                    result = userService.createUser(user);
                    if(result != null) response.getWriter().append(result);
                    break;
                case "delete":

                    break;
                default:
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().append("Unknown action");
            }
        }
//        response.setContentType("application/json");
//        response.getWriter().append(mapper.writeValueAsString((new UserDao()).findByUserName("test")));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
