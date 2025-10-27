package com.terrideboer.bookbase.models;

import com.terrideboer.bookbase.models.enums.LoanStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate loanDate;

    private LocalDate returnDate;

    private Integer loanPeriodInDays = 21;

    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus = LoanStatus.ACTIVE;

    @OneToOne(mappedBy = "loan", cascade = CascadeType.ALL)
    private Fine fine;

    @ManyToOne
    @JoinColumn(name = "book_copy_id", nullable = false)
    private BookCopy bookCopy;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public Integer getLoanPeriodInDays() {
        return loanPeriodInDays;
    }

    public void setLoanPeriodInDays(Integer loanPeriodInDays) {
        this.loanPeriodInDays = loanPeriodInDays;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public Fine getFine() {
        return fine;
    }

    public void setFine(Fine fine) {
        this.fine = fine;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
