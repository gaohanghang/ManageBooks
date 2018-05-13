package cn.lightina.managebooks.service;

import cn.lightina.managebooks.Exception.AddBookListException;
import cn.lightina.managebooks.Exception.ReservationException;
import cn.lightina.managebooks.dao.BookMapper;
import cn.lightina.managebooks.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BookService implements BookServiceimpl {
    @Autowired(required = false)
    BookMapper bookMapper;

    /*user*/
    @Override
    public List<BookList> getlist() {
        return bookMapper.getList();
    }

    @Override
    public List<BookList> getlistByQuery(String query) {
        return bookMapper.getListByQuery(query);
    }

    @Override
    public List<Book> getlistByuId(int userId) {
        return null;
    }

    @Override
    @Transactional
    public Reservation processRes(String ISBN, User user) {
        List<Book> list = bookMapper.getRes(ISBN);
        if (list == null) {
            throw new ReservationException("预约失败");
        }
        Book book=list.get(0);
        int state=book.getState();
        int count=0;
        if(state==0)
            count = bookMapper.insertResInfo(book, user);
        else if(state==2)
            count = bookMapper.insertResInfoNull(book,user);
        if (count <= 0) {
            throw new ReservationException("预约失败");
        }
        // TODO: 2018/5/13 czh 预约成功时需要改变对应图书状态
        if(state==0)bookMapper.changeBookState(book,1);
        Reservation r = bookMapper.getResId(book, user);
        return r;
    }

    @Override
    public int deleteByBId(int bookId) {
        /*
         * 先根据uid bookid => borrowid
         * update borrow enddate
         * */
        return 0;
    }

    /**/

    @Override
    public int addBookList(BookList bookList) {
        int count = bookMapper.addBookList(bookList);
        if (count <= 0) throw new AddBookListException("添加失败");
        return count;
    }

    @Override
    public Book insertBorrow(BookList booklist, int userId) {
        return null;
    }

    @Override
    public List<ReservationDetail> getResById(User user) {
        List<ReservationDetail>list=bookMapper.getResById(user);
        for(ReservationDetail rd:list){
            int bookId=rd.getBookId();

            Date deadline=rd.getDeadline();

        }
        return list;
    }

    @Override
    public List<Reservation> getResInfo() {
        return null;
    }

    @Override
    public List<BorrowDetail> getBorInfo(User user) {
        return bookMapper.getBorById(user);
    }

    @Override
    public List<ReservationDetail> getResList() {
        return bookMapper.getResList();
    }
}
