import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class BookDAO {
	
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;
	
	public BookDAO (String jdbcURL, String jdbcUsername, String jdbcPassword) 
	{
		this.jdbcURL=jdbcURL;
		this.jdbcUsername=jdbcUsername;
		this.jdbcPassword=jdbcPassword;
	}
	protected void connect() throws SQLException 
	{
		if (jdbcConnection == null || jdbcConnection.isClosed()) 
		{
			
			try 
			{
				Class.forName ("com.mysql.Driver");
			}
			catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
			
		}
	}
	protected void disconnect () throws SQLException
	{
		if (jdbcConnection != null && !jdbcConnection.isClosed()) 
		{
			jdbcConnection.close();
		}
	}
	
	public boolean insertBook (Book books) throws SQLException {
		String sql="INSERT INTO (title, author, price) VALUES (?,?,?,?)";
		connect();
		PreparedStatement statement = (PreparedStatement) jdbcConnection.prepareStatement(sql);
		statement.setString(1, books.getTitle());
		statement.setString(2, books.getAuthor());
		statement.setFloat(3, books.getPrice());
		
		boolean rowInserted = statement.executeUpdate() > 0;
		statement.close();
		disconnect();
		return rowInserted;
	}
	public List <Book> listAllBooks() throws SQLException {
		List <Book> listBook= new ArrayList<>();
		
		String sql = "SELECT * FROM books";
		
		connect();
        
        Statement statement = (Statement) jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
         
        while (resultSet.next()) {
            int id = resultSet.getInt("book_id");
            String title = resultSet.getString("title");
            String author = resultSet.getString("author");
            float price = resultSet.getFloat("price");
             
            Book book = new Book(id, title, author, price);
            listBook.add(book);
        }
         
        resultSet.close();
        statement.close();
         
        disconnect();
         
        return listBook;
  
		
		
	}
	 public boolean deleteBook(Book book) throws SQLException {
	        String sql = "DELETE FROM books where book_id = ?";
	         
	        connect();
	         
	        PreparedStatement statement = (PreparedStatement) jdbcConnection.prepareStatement(sql);
	        statement.setInt(1, book.getId());
	         
	        boolean rowDeleted = statement.executeUpdate() > 0;
	        statement.close();
	        disconnect();
	        return rowDeleted;     
	    }
	     
	    public boolean updateBook(Book book) throws SQLException {
	        String sql = "UPDATE books SET title = ?, author = ?, price = ?";
	        sql += " WHERE book_id = ?";
	        connect();
	         
	        PreparedStatement statement = (PreparedStatement) jdbcConnection.prepareStatement(sql);
	        statement.setString(1, book.getTitle());
	        statement.setString(2, book.getAuthor());
	        statement.setFloat(3, book.getPrice());
	        statement.setInt(4, book.getId());
	         
	        boolean rowUpdated = statement.executeUpdate() > 0;
	        statement.close();
	        disconnect();
	        return rowUpdated;     
	    }
	     
	    public Book getBook(int id) throws SQLException {
	        Book book = null;
	        String sql = "SELECT * FROM books WHERE book_id = ?";
	         
	        connect();
	         
	        PreparedStatement statement = (PreparedStatement) jdbcConnection.prepareStatement(sql);
	        statement.setInt(1, id);
	         
	        ResultSet resultSet = statement.executeQuery();
	         
	        if (resultSet.next()) {
	            String title = resultSet.getString("title");
	            String author = resultSet.getString("author");
	            float price = resultSet.getFloat("price");
	             
	            book = new Book(id, title, author, price);
	        }
	         
	        resultSet.close();
	        statement.close();
	         
	        return book;
	    }
	
	

}
