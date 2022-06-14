package datamodel;


import java.util.*;

/**
 * Class of a line item as part of an Order. Orders may have multiple order items.
 * 
 * @version <code style=color:green>{@value application.package_info#Version}</code>
 * @author <code style=color:blue>{@value application.package_info#Author}</code>
 */
public class OrderItem {

    /**
     * Ordered article, is never null.
     */
    private final Article article;

    /**
     * Number of units ordered, is always a positive number {@code > 0}.
     */
    private int unitsOrdered;

    /**
     * Constructor of ordered line item with article and units arguments.
     * @param article ordered article, throws IllegalArgumentException if article is null
     * @param unitsOrdered number of articles ordered
     */
    protected OrderItem(Article article, int unitsOrdered) {
    	if(article == null) {
    		throw new IllegalArgumentException();
    	}
    	this.article = article;
    	this.unitsOrdered = unitsOrdered;
    }

    /**
     * Article getter.
     * @return ordered article.
     */
    public Article getArticle() {
        return article;
    }

    /**
     * UnitsOrdered getter.
     * @return number of article ordered.
     */
    public int getUnitsOrdered() {
        return unitsOrdered;
    }

    /**
     * UnitsOrdered setter.
     * @param units units updated number of articles ordered, must be {@code >= 0}.
     * @throws IllegalArgumentException if units not a positive {@code units >0} number.
     */
    public void setUnitsOrdered(int units) {
    	if(unitsOrdered < 0) {
    		throw new IllegalArgumentException("nope");
    	}
    	this.unitsOrdered = units;
    }

}
