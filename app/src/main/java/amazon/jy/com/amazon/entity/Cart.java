package amazon.jy.com.amazon.entity;

/**
 * Created by jiangy on 18-4-22.
 */

public class Cart {
    private Integer cId;
    private Integer uId;
    private Book book;

    public Integer getcId() {
        return cId;
    }

    public void setcId(Integer cId) {
        this.cId = cId;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
