package controller;

import DAO.AccountDAO;
import DAO.CustomerDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Account;
import model.Customer;
import util.MaHoaMK;

import java.io.IOException;
import java.util.List;

@WebServlet("/CustomerController")
public class CustomerController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public CustomerController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String hanhDong = request.getParameter("hanhDong");
        if(hanhDong.equals("login")){
            Login(request, response);
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private void Login(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            password = MaHoaMK.toSHA1(password);

            String url = "";
            AccountDAO accountDAO = new AccountDAO();
            CustomerDAO customerDAO = new CustomerDAO();
            List<Account> accountList= accountDAO.selectByCondition("username='"+username+"' and password='"+password+"'");
            Account account = accountList.size()!=0?accountList.get(0):null;
            if(account != null){
                List<Customer> customerList = customerDAO.selectByCondition("username='"+username+"'");
                Customer customer = customerList.size()!=0?customerList.get(0):null;
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("password", password);
                session.setAttribute("customer", customer);
                url = "/index.jsp";
            }
            else {
                request.setAttribute("baoLoi", "Tên đăng nhập hoặc mật khẩu không đúng");
                url = "/customer/login.jsp";
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}