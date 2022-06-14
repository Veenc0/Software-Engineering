package system.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;
import datamodel.OrderItem;
import datamodel.TAX;
import system.Calculator;
import system.Formatter;
import system.Printer;
import system.TablePrinter;
import system.TablePrinter.Builder;
import system.impl.FormatterImpl;


class PrinterImpl implements Printer {
	
	private final Calculator calculator;
	private final Formatter formatter;

	PrinterImpl(Calculator calculator, Formatter formatter) {
		this.calculator = calculator;
		this.formatter = formatter;
	}
	
	public <T,R> R process(final R collector, final Collection<T> collection,
			final Consumer<T> applyEach
	) {
		return process(collector, collection, null, applyEach);
	}
	
	public <T,R> R process(final R collector, final Collection<T> collection,
			final Function<Stream<T>, Stream<T>> callout,
			final Consumer<T> applyEach
	) {
		if(collection != null) {
			(callout != null? callout.apply(collection.stream()) : collection.stream())
				.forEach(t -> applyEach.accept(t));
		}
		return collector;
	}

	@Override
	public TablePrinter createTablePrinter(StringBuffer sb, Consumer<Builder> builder) {
		return new TablePrinterImpl(sb, builder);
	}

	@Override
	public StringBuffer printCustomer(StringBuffer sb, Customer c) {
		if(c==null)
			return sb;
		//
		final StringBuffer contacts = new StringBuffer();
		IntStream.range(0, c.contactsCount()).forEach(i ->
			contacts.append(i==0? "" : ", ").append(c.getContacts()[i])
		);
		//
		int nameStyle = 0;
		return (sb==null? new StringBuffer() : sb)
			.append(String.format("| %6d ", c.getId()))
			.append(String.format("| %-31s", formatter.fmtName(c.getFirstName(), c.getLastName(), nameStyle)))
			.append(String.format("| %-44s ", contacts))
			.append("|\n");
	}

	@Override
	public StringBuffer printCustomers(StringBuffer sb, Collection<Customer> customers) {
		if(customers==null)
			return sb;
		//
		final StringBuffer sb_ = sb==null? new StringBuffer() : sb;
//		var cit = customers.iterator();
//		while(cit.hasNext()) {		// iterate through Customer collection
//			Customer c = cit.next();
//			printCustomer(sb, c);		// format each Customer object as line into StringBuffer
//		}
//		return sb;
		return process(sb_, customers, s -> s, c -> printCustomer(sb_, c));	// calling generic print method
	}

	@Override
	public StringBuffer printArticle(StringBuffer sb, Article a) {
		if(a==null)
			return sb;
		//
		return (sb==null? new StringBuffer() : sb)
			.append(String.format("| %10s ", a.getId()))
			.append(String.format("| %-27s", a.getDescription()))
			.append(String.format("| %6d ", a.getUnitPrice())).append("\u20ac")	// Unicode for Euro
			.append(String.format("| %4s MwSt", a.getTax()==TAX.GER_VAT_REDUCED? "7%" : "19%"))
			.append("|\n");
	}

	@Override
	public StringBuffer printArticles(StringBuffer sb, Collection<Article> articles) {
		if(articles==null)
			return sb;
		//
		final StringBuffer sb_ = sb==null? new StringBuffer() : sb;
		return process(sb_, articles, a -> printArticle(sb_, a));
	}

	@Override
	public StringBuffer printOrder(StringBuffer sb, Order order) {
		if(order==null)
			return sb;
		//
		final String creationDate = formatter.fmtDate(order.getCreationDate(), 0, "");
		final Customer c = order.getCustomer();
		return (sb==null? new StringBuffer() : sb)
			.append(String.format("| %10s ", order.getId()))
			.append(String.format("| %-27s", formatter.fmtName(c.getFirstName(), c.getLastName(), 0)))
			.append(String.format("| %1d items ", order.itemsCount()))
			.append(String.format("| created: %s ", creationDate))
			.append("|\n");
	}

	@Override
	public StringBuffer printOrders(StringBuffer sb, Collection<Order> orders) {
		if(orders==null)
			return sb;
		//
		final StringBuffer sb_ = sb==null? new StringBuffer() : sb;
		return process(sb_, orders, a -> printOrder(sb_, a));
	}

	@Override
	public TablePrinter printOrder(TablePrinter orderTable, Order order) {
		if(orderTable != null && order != null) {
			String id = order.getId();	// retrieve order attributes
			String name = order.getCustomer().getFirstName();
			orderTable.row(id, name + "'s Bestellung: ");
			long totalPrice = 0;
			long totalMwst = 0;
			Iterator<OrderItem> a = order.getItems().iterator();
			while(a.hasNext()) {
				OrderItem element = a.next();
				long price = element.getArticle().getUnitPrice(); //price of one article
				int manyorders = element.getUnitsOrdered(); // how many times it was ordered
				String nameOfArticle = element.getArticle().getDescription(); //what kind of article
				String itemPriceStr = formatter.fmtPrice(element.getArticle().getUnitPrice()); //format price in string
				String sumItemPriceStr = formatter.fmtPrice(manyorders * price, 1); //number of articles * price of article
				long newnew = manyorders * price; //number of articles * price
				long orderVAT = calculator.calculateIncludedVAT(newnew, element.getArticle().getTax()); //calculate the tax of the total price
				String itemVATStr = formatter.fmtPrice(orderVAT); //format long in a string for total tax of each article order
				totalPrice += newnew;
				totalMwst += orderVAT;
				String totaloprico = formatter.fmtPrice(totalPrice, 1);
				String sumMWT = formatter.fmtPrice(totalMwst, 1);
				String reducedTaxMarker = "";
				String finalOne = "";
				if(manyorders == 1) {
					finalOne = manyorders + " " + nameOfArticle;
				}
				else {
					finalOne = manyorders + " " + nameOfArticle + ", " + manyorders + "x" + " " + itemPriceStr;
				}
				if(element.getArticle().getTax() == TAX.GER_VAT_REDUCED) {
					reducedTaxMarker = "*";
				} else {
					reducedTaxMarker = "";
				}
				if(a.hasNext()) {
					orderTable.row("", " - " + finalOne , itemVATStr, reducedTaxMarker,  sumItemPriceStr, "" , "" );
				}else {
			orderTable.row("", " - " + finalOne , itemVATStr, reducedTaxMarker,  sumItemPriceStr, sumMWT , totaloprico );
				}
			}
		}
		return orderTable;
	}

	@Override
	public TablePrinter printOrders(TablePrinter orderTable, Collection<Order> orders) {
		long[] totals = {	// tuple with compounded price and VAT tax values over all orders.
				0L,		// compounded order value stored in first element
				0L		// compounded order VAT tax stored in second element
		};
		Consumer<Order> ff = a->printOrder(orderTable, a).line(); 
		long totalPricee = 0;
		long totalMwst = 0;
		for(Order order : orders) {
//			printOrder(orderTable, order).line();
			for(OrderItem e : order.getItems()) {
				long price = e.getArticle().getUnitPrice();
				int manyorders = e.getUnitsOrdered();
				long newnew = manyorders * price; 
				totalPricee += newnew;
				long orderVAT = calculator.calculateIncludedVAT(newnew, e.getArticle().getTax());
				totalMwst += orderVAT;
				totals[0] = totalPricee;
				totals[1] = totalMwst;
			}
		}
		String totalPrice = formatter.fmtPrice(totals[0], 1);
		String totalVAT = formatter.fmtPrice(totals[1], 1);
		//return orderTable
		return process(orderTable, orders, s -> s.sorted((a,b) -> Long.compare(b.bla(), a.bla())),ff)
			.row( "@ >        |   |", "", "", "", "", "Gesamt:", totalVAT, totalPrice)
			.line("@          +=+=+");
	}
}
