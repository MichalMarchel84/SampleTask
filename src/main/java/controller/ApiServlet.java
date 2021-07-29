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

        //Checking if "action" parameter is present and making decision on which action to perform
        if(action != null){
            switch (action){
                case "login":
                    login(request, response);
                    break;

                case "new":
                    newEntry(request, response);
                    break;

                case "new_user":
                    newUser(request, response);
                    break;

                case "delete":
                    deleteEntry(request, response);
                    break;

                default:

                    //If unknown "action" parameter passed, setting response status to "BAD REQUEST" and appending error message
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().append(mapper.writeValueAsString(new ErrorMessage("Unknown action")));
            }
        }else {

            //If "action" parameter missing, responding with list of blog entries
            response.getWriter().append(mapper.writeValueAsString(blogService.getAll()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //Attempting to login user with given parameters.
        try {
            User user = userService.login(request.getParameter("user"), request.getParameter("password"));
            //Creating session if not exists and storing user as session attribute
            request.getSession().setAttribute("user", user);
        }catch (LoginException e) {
            //If login fails, sending message with the reason
            response.getWriter().append(mapper.writeValueAsString(new ErrorMessage(e.getMessage())));
        }
    }

    private void newEntry(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //Attempting to persist new blog entry in DB.
        BlogEntry entry = new BlogEntry();
        entry.setText(request.getParameter("text"));
        String result = blogService.createEntry(entry, (User) request.getSession().getAttribute("user"));

        //If attempt fails, error message is returned and sent as response body
        if(result != null) response.getWriter().append(mapper.writeValueAsString(new ErrorMessage(result)));
    }

    private void newUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //Creating user instance and setting it's fields with values from request
        User user = new User();
        user.setUserName(request.getParameter("user"));
        user.setPassword(request.getParameter("password"));
        user.setPermission(request.getParameter("permission"));
        user.setReadOnly(request.getParameter("readonly"));

        //Attempting to persist new user in DB
        String result = userService.createUser(user);

        //If attempt fails, error message is returned and sent as response body
        if(result != null) response.getWriter().append(mapper.writeValueAsString(new ErrorMessage(result)));
    }

    private void deleteEntry(HttpServletRequest request, HttpServletResponse response) throws IOException{
        //Attempting to delete blog entry
        try {
            String result = blogService.deleteEntry(Integer.parseInt(request.getParameter("id")), (User) request.getSession().getAttribute("user"));

            //If attempt fails, error message is returned and sent as response body
            if (result != null) response.getWriter().append(mapper.writeValueAsString(new ErrorMessage(result)));
        }catch (NumberFormatException e){

            //If parsing "id" parameter fails, setting response status to "BAD REQUEST" and appending error message
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().append(mapper.writeValueAsString(new ErrorMessage("Wrong id format")));
        }
    }
}
