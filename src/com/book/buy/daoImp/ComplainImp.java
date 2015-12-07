package com.book.buy.daoImp;

import com.book.buy.dao.ComplainDao;
import com.book.buy.utils.DBUtils;
import com.book.buy.vo.ComplainVo;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by violet on 2015/10/28.
 * 15/11/11 修改：添加关于申诉的方法
 * 2015/11/26 修改：添加通过ID查找申诉和投诉的方法
 * 2015/12/4  修改：把申诉和投诉的方法合在了一起
 */
public class ComplainImp implements ComplainDao {
    private QueryRunner runner = null;
    private Connection conn = null;

    public ComplainImp(){
        runner = new QueryRunner();
        conn = DBUtils.getConnection();
    }

    @Override
    public void addComp(ComplainVo comp) throws SQLException {
        //添加投诉
        String sql = "insert into complain(userID, bookID, description, complainType) values(?, ?, ?, ?)";
        runner.update(conn, sql, comp.getUserid(), comp.getBookid(), comp.getDescription(), 0);

        //user表中的complainNum加1
        String sql1 = "update user set complainNum = complainNum+1";
        runner.update(conn, sql1);
    }

    @Override
    public void addAppeal(ComplainVo comp) throws SQLException {
        //添加申诉
        String sql = "insert into complain(userID, bookID, description, complainType) values(?, ?, ?, ?)";
        runner.update(conn, sql, comp.getUserid(), comp.getBookid(), comp.getDescription(), 1);
    }

    @Override
    public void updateComp(ComplainVo comp) throws SQLException {
        //更新投诉、申诉
        String sql = "update complain set userID = ?, bookID = ?, description = ?, " +
                "complainType = ?, state = ?";
        runner.update(conn, sql, comp.getUserid(), comp.getBookid(), comp.getDescription(),
                comp.getComplainType(), comp.getState());
    }

    @Override
    public List<ComplainVo> getAllComp() throws SQLException {
        String sql = "select id, userID, bookID, description, complainType, " +
                "state from complain where complainType = 0";
        return runner.query(conn, sql, new BeanListHandler<ComplainVo>(ComplainVo.class));
    }

    @Override
    public List<ComplainVo> getAllAppeal() throws SQLException {
        String sql = "select id, userID, bookID, description, complainType, " +
                "state from complain where complainType = 1";
        return runner.query(conn, sql, new BeanListHandler<ComplainVo>(ComplainVo.class));
    }

    @Override
    public List<ComplainVo> getCompByUserid(int uid) throws SQLException {
        String sql = "select id, userID, bookID, description, complainType, " +
                "state from complain where complainType = 0 and userID = ?";
        return runner.query(conn, sql, new BeanListHandler<ComplainVo>(ComplainVo.class), uid);
    }

    @Override
    public List<ComplainVo> getCompByState(int state) throws SQLException {
        String sql = "select id, userID, bookID, description, complainType, " +
                "state from complain where complainType = 0 and state = ?";
        return runner.query(conn, sql, new BeanListHandler<ComplainVo>(ComplainVo.class), state);
    }

    @Override
    public ComplainVo getCompById(int id) throws SQLException {
        String sql = "select id, userID, bookID, description, complainType, " +
                "state from complain where id = ?";
        return runner.query(conn, sql, new BeanHandler<ComplainVo>(ComplainVo.class), id);
    }

    @Override
    public void close() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
