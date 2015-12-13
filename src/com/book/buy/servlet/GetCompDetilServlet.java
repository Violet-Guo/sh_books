package com.book.buy.servlet;

import com.book.buy.dao.BookDao;
import com.book.buy.dao.ComplainDao;
import com.book.buy.dao.UserDao;
import com.book.buy.factory.BookDaoImpFactory;
import com.book.buy.factory.ComplainDaoImpFactory;
import com.book.buy.factory.UserDaoImpFactory;
import com.book.buy.vo.BookVo;
import com.book.buy.vo.ComplainVo;
import com.book.buy.vo.ManagerVo;
import com.book.buy.vo.UserVo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Created by violet on 2015/11/26.
 */
@WebServlet(name = "GetCompDetilServlet", urlPatterns = "/getcompdetil")
public class GetCompDetilServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");

        PrintWriter out = response.getWriter();
        String href = "";

        //校验管理员的登陆状态
        ManagerVo admin = (ManagerVo)request.getSession().getAttribute("admin");
        if (null == admin){
            href = "/loginmanager";
            out.print("<script language='javascript'>alert('登录状态失效，管理员请登陆！');"
                    + "window.location.href='" + href + "';</script>");
            return;
        }

        //拿到jsp页面中传来的compid的值，并将其转换成int型
        String compid = request.getParameter("compid");
        int id = Integer.parseInt(compid);

        UserVo uservo = new UserVo();
        BookVo bookvo = new BookVo();
        ComplainVo compvo = new ComplainVo();

        UserDao userdao = UserDaoImpFactory.getUserDaoImpl();
        BookDao bookdao = BookDaoImpFactory.getBookDaoImpl();
        ComplainDao compdao = ComplainDaoImpFactory.getCompDaoImp();

        //查找到投诉的详情、被投诉人、被投诉的ID
        try {
            compvo = compdao.getCompById(id);
            uservo = userdao.findUserById(compvo.getUserid());
            bookvo = bookdao.findById(compvo.getBookid());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //把拿到的数据放在session中
        request.getSession().setAttribute("compuser", uservo);
        request.getSession().setAttribute("compdetil", compvo);
        request.getSession().setAttribute("compbook", bookvo);

        bookdao.close();
        compdao.close();

        response.sendRedirect("/compdetil");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
