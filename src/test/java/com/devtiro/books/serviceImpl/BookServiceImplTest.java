package com.devtiro.books.serviceImpl;

import com.devtiro.books.domain.Book;
import com.devtiro.books.domain.BookEntity;
import com.devtiro.books.repository.BookRepository;
import com.devtiro.books.services.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.devtiro.books.TestData.testBook;
import static com.devtiro.books.TestData.testBookEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl underTest;

    @Test
    public void testThatBookIsSaved() {
        final Book book = testBook();

        final BookEntity bookEntity = testBookEntity();

        when(bookRepository.save((bookEntity))).thenReturn(bookEntity);

        final Book result = underTest.save(book);
        assertEquals(book, result);
    }

    @Test
    public void testThatFindByIdReturnsEntityWhenNoBook() {
        final String isbn = "123123123";
        when(bookRepository.findById((isbn))).thenReturn(Optional.empty());

        final  Optional<Book> result = underTest.findById(isbn);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testThatFindByIdReturnsBookWhenExists() {
        final Book book = testBook();
        final BookEntity bookEntity = testBookEntity();
        when(bookRepository.findById((book.getIsbn()))).thenReturn(Optional.of(bookEntity));

        final  Optional<Book> result = underTest.findById(book.getIsbn());
        assertEquals(Optional.of(book), result);
    }

    @Test
    public void testListBooksReturnsEmptyListWhenNoBooksExist() {
        when(bookRepository.findAll()).thenReturn(new ArrayList<BookEntity>());
        final List<Book> result = underTest.listBooks();
        assertEquals(0, result.size());
    }

    @Test
    public void testListBooksReturnsEmptyListWhenExist() {
        final BookEntity bookEntity = testBookEntity();
        when(bookRepository.findAll()).thenReturn(List.of(bookEntity));
        final List<Book> result = underTest.listBooks();
        assertEquals(1, result.size());
    }

    @Test
    public void testIsBookExistReturnsFalseWhenBookDoesntExist() {
        when(bookRepository.existsById(any())).thenReturn(false);
        final boolean result = underTest.isBookExist(testBook());
        assertEquals(false, result);
    }

    @Test
    public void testIsBookExistReturnsTrueWhenBookDoesExist() {
        when(bookRepository.existsById(any())).thenReturn(true);
        final boolean result = underTest.isBookExist(testBook());
        assertEquals(true, result);
    }

    @Test
    public void testDeleteBookDeletesBooks() {
        final String isbn = "123123123";
        underTest.deleteBookById(isbn);
        verify(bookRepository, times(1)).deleteById(isbn);
    }
}
