package net.droid;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HSSCheckout {

	/*
	 * We would like you to enhance this Java class to perform the function of a
	 * simple shopping cart checkout. Consider what you think is the best design
	 * for this class and then build the code. You should spend less than 90
	 * minutes on this exercise.
	 *	 
	 * The class has a single public method 'price' which accepts a string of
	 * stock codes. It should return the calculated total price as a BigDecimal.
	 *
	 * Each item in the shopping basket is represented by a single character in
	 * the string. So, for example, a shopping basket of > one item of type A >
	 * three items of type B > one item of type C would be represented by
	 * checkout.price("ABBBC")
	 *
	 * The initial pricing model is defined below: PRICE LIST StockCode price
	 * offers A 1.99 - B 3.23 buy TWO, get a THIRD one free C 0.59 - D 2.30 Two
	 * for 4.00
	 *
	 * Using the example and pricing model shown above, checkout.price("ABBBC")
	 * would return a value of 9.04 (1.99 + 2 x 3.23 + 0.59 = 9.04 n.b. the
	 * third "B" item is free)
	 *
	 * The total price should be returned as a BigDecimal, accurate to two
	 * decimal places.
	 *
	 */

	public BigDecimal price(String goods) {

		BigDecimal finalPrice = BigDecimal.ZERO;
		BigDecimal interPrice = BigDecimal.ONE;
		BigDecimal one = BigDecimal.ONE;

		// price list
		// StockCode price offers on (min) quantity lower price free
		// {A, 1.99, 0, 0, 0}
		// {B, 3.23, 2, 0, 1}
		// {C, 0.59, 0, 0, 0}
		// {D, 2.30, 2, 4.00, 0}
		List<Product> productList = new ArrayList<Product>();

		productList.add(new Product("A", BigDecimal.valueOf(1.99), 0, BigDecimal.valueOf(0.00), 0));
		productList.add(new Product("B", BigDecimal.valueOf(3.23), 2, BigDecimal.valueOf(0.00), 1));
		productList.add(new Product("C", BigDecimal.valueOf(0.59), 0, BigDecimal.valueOf(0.00), 0));
		productList.add(new Product("D", BigDecimal.valueOf(2.30), 2, BigDecimal.valueOf(4.00), 0));
		
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();

		for (int i = 0; i < goods.length(); i++) {
			char c = goods.charAt(i);
			Integer val = map.get(new Character(c));
			if (val != null) { // if not null, set already
				map.put(c, new Integer(val + 1));
			} else { //none yet
				map.put(c, 1); //
			}
		}

		for (Map.Entry<Character, Integer> entry : map.entrySet()) {			
			Iterator<Product> itr = productList.iterator();

			while (itr.hasNext()) {
				Product element = itr.next();
				if (entry.getKey().toString().equals((element.name).toString())) { //start
					// start count Total Price!
					if (element.minQuantity > 0) { // indicates about DISCOUNTS of any type exists
						if ( entry.getValue() >= element.minQuantity ) //satisfied condition - necessary quantity of particular product
						{ 
							if ( element.extraQuantityForFree > 0 ) //if exists extraQuantityForFree
							{ 
								interPrice = interPrice.multiply(new BigDecimal(entry.getValue()));
								interPrice = interPrice.multiply(element.price);
								//then subtract extraQuantityForFree price
								interPrice = interPrice.subtract(element.price); //COULD BE ENHANCED ADDING CYCLE FOR MORE THAN ONE QUANTITY
							}	
							else //the only field for DISCOUNTS left i.e. priceOnMinQuantity
							{
								interPrice = interPrice.multiply(new BigDecimal(entry.getValue()));
								interPrice = interPrice.multiply(element.priceOnMinQuantity);
							}						
						}//end necessary quantity of products for discount
						else{ //not enough quantity to get DISCOUNT
							interPrice = interPrice.multiply(new BigDecimal(entry.getValue()));
							interPrice = interPrice.multiply(element.price);
						}
					} //end of DISCOUNT
					else {
						interPrice = interPrice.multiply(new BigDecimal(entry.getValue()));
						interPrice = interPrice.multiply(element.price);
					}

					finalPrice = finalPrice.add(interPrice);
					interPrice = one;
				}//end
			}
		}
		return finalPrice.setScale(2, RoundingMode.HALF_EVEN);
	}

	public static void main(String[] args) {
		//
		HSSCheckout checkout = new HSSCheckout();
		System.out.println("Total price: " + checkout.price("ABBBC"));
	}

}

//Class for Product 
class Product {
	String name; //product name
	BigDecimal price; //regular price
	Integer minQuantity; //field that indicates minimum quatity to get discount. If there is no discount then quantity = 0
	BigDecimal priceOnMinQuantity; //price if satisfied minimum quantity condition
	Integer extraQuantityForFree;  // 

	public Product(String name, BigDecimal price, Integer minQuantity, BigDecimal priceOnMinQuantity,
			Integer extraQuantityForFree) {
		this.name = name;
		this.price = price;
		this.minQuantity = minQuantity; 
		this.priceOnMinQuantity = priceOnMinQuantity;
		this.extraQuantityForFree = extraQuantityForFree;
	}
}
