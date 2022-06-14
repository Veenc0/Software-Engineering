package system;

import java.util.List;
import java.util.Optional;

import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;

public interface DatamodelFactory {
	
	public Customer createCustomer();
	
	public Customer createCustomer(String name);
	
	public Article createArticle();
	
	public Article createArticle(String description, long unitPrice);
	
	public Order createOrder(Customer customer);
	
	public List<Customer> getCustomers();
	
	public List<Article> getArticles();
	
	public List<Order> getOrders();
	
	public int customersCount();
	
	public int articlesCount();
	
	public Optional<Customer> findCustomerById(long id);
	
	public Optional<Article> findArticleById(String id);
	
	public Optional<Order> findOrderById(String id);
	
	public int ordersCount();
	
}
