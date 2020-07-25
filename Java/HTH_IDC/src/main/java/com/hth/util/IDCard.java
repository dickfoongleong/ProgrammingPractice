package com.hth.util;
import java.awt.Image;;

public class IDCard {
	/**
	 * Instance variables declaration.
	 */
	private String group;
	private String div;
	private String cardNumber;
	private String description;
	private String[] frontTexts; // Array of Texts for the Front Lines.
	private String[] backTexts; // Array of Texts for the Back Lines.
	private String[] frontTextModifiers; // Array of Modifiers for the Front Lines.
	private String[] backTextModifiers; // Array of Modifiers for the Back Lines.
	private Image[] frontLogo; // Array of Logos for the Front.
	private Image[] backLogo; // Array of Logos for the Back.
	private boolean isReady; // Indicator of ID Card is all set.
	private IDMask[] frontMask;
	private IDMask[] backMask;
	private String frontLarge;
	private String backLarge;
	private String[] frontLogoVars;
	private String[] backLogoVars;

	/**
	 * Constructor of ID Card instance.
	 *
	 * @param frontTexts
	 * 		Text on 18 Lines of the Front.
	 * @param backTexts
	 *		Text on 18 Lines of the Back.
	 * @param frontTextModifiers
	 *		Text Modifier flags of the Front.
	 * @param backTextModifiers
	 *		Text Modifier flags of the Back.
	 * @param frontMask
	 * 		Array of masks applied on the Front.
	 * @param backMask
	 * 		Array of masks applied on the Back.
	 * @param frontLarge
	 * 		Flag indicates to use large logo on Front. (H/V) for Horizontal or Vertical.
	 * @param backLarge
	 * 		Flag indicates to use large logo on Back. (H/V) for Horizontal or Vertical.
	 */
	public IDCard(String[] frontTexts, String[] backTexts, String[] frontTextModifiers, String[] backTextModifiers, IDMask[] frontMask, IDMask[] backMask, String frontLarge, String backLarge) {
		this.frontTexts = frontTexts;
		this.backTexts = backTexts;
		this.frontTextModifiers = frontTextModifiers;
		this.backTextModifiers =backTextModifiers;
		this.isReady = false;
		this.frontMask = frontMask;
		this.backMask = backMask;
		this.frontLarge = frontLarge;
		this.backLarge = backLarge;
	}

	/**
	 * Constructor of ID card instance with full details.
	 * 
	 * @param group
	 * 		Group number.
	 * @param div
	 * 		Division number.
	 * @param cardNumber
	 * 		Number of the card.
	 * @param description
	 * 		Description of the card.
	 * @param frontTexts
	 * 		Text on 18 Lines of the Front.
	 * @param backTexts
	 *		Text on 18 Lines of the Back.
	 * @param frontTextModifiers
	 *		Text Modifier flags of the Front.
	 * @param backTextModifiers
	 *		Text Modifier flags of the Back.
	 * @param frontMask
	 * 		Array of masks applied on the Front.
	 * @param backMask
	 * 		Array of masks applied on the Back.
	 * @param frontLarge
	 * 		Flag indicates to use large logo on Front. (H/V) for Horizontal or Vertical.
	 * @param backLarge
	 * 		Flag indicates to use large logo on Back. (H/V) for Horizontal or Vertical.
	 */
	public IDCard(String group, String div, String cardNumber, String description,
			String[] frontTexts, String[] backTexts, String[] frontTextModifiers, String[] backTextModifiers, IDMask[] frontMask, IDMask[] backMask, String frontLarge, String backLarge,
			String[] frontLogoVars, String[] backLogoVars) {
		this.group = group;
		this.div = div;
		this.cardNumber = cardNumber;
		this.description = description;
		this.frontTexts = frontTexts;
		this.backTexts = backTexts;
		this.frontTextModifiers = frontTextModifiers;
		this.backTextModifiers =backTextModifiers;
		this.isReady = false;
		this.frontMask = frontMask;
		this.backMask = backMask;
		this.frontLarge = frontLarge;
		this.backLarge = backLarge;

		this.frontLogoVars = frontLogoVars;
		this.backLogoVars = backLogoVars;
	}

	/**
	 * Constructor of ID card instance with number and description.
	 * 
	 * @param group
	 * 		Group number.
	 * @param div
	 * 		Division number.
	 * @param cardNumber
	 * 		Number of the card.
	 * @param description
	 * 		Description of the card.
	 */
	public IDCard(String group, String div, String cardNumber, String description) {
		this.group = group;
		this.div = div;
		this.cardNumber = cardNumber;
		this.description = description;
		this.frontTexts = initStr(18);
		this.backTexts = initStr(18);
		this.frontTextModifiers = initStr(18);
		this.backTextModifiers = initStr(18);
		this.frontLogo = new Image[9];
		this.backLogo = new Image[9];
		this.isReady = true;
		this.frontMask = new IDMask[0];
		this.backMask = new IDMask[0];
		this.frontLarge = "";
		this.backLarge = "";
		this.frontLogoVars = initStr(9);
		this.backLogoVars = initStr(9);
	}

	/**
	 * Get the group number.
	 * 
	 * @return
	 * 		The group number.
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * Get the division number.
	 * 
	 * @return
	 * 		The division number.
	 */
	public String getDiv() {
		return div;
	}

	/**
	 * Get the number of the card.
	 * 
	 * @return
	 * 		The card number.
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * Get the description of the card.
	 * 
	 * @return
	 * 		The description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the front 18 lines of texts.
	 *
	 * @return
	 *      The front texts.
	 */
	public String[] getFrontTexts() {
		return frontTexts;
	}

	/**
	 * Get the back 18 line of texts.
	 *
	 * @return
	 *      The back texts.
	 */
	public String[] getBackTexts() {
		return backTexts;
	}

	/**
	 * Get the front 18 lines of texts modifiers.
	 *
	 * @return
	 *      The front texts modifiers.
	 */
	public String[] getFrontTextModifiers() {
		return frontTextModifiers;
	}

	/**
	 * Get the back 18 lines of texts modifiers.
	 *
	 * @return
	 *      The back texts modifiers.
	 */
	public String[] getBackTextModifiers() {
		return backTextModifiers;
	}

	/**
	 * Get the front 9 logos.
	 *
	 * @return
	 *      The front logos.
	 */
	public Image[] getFrontLogo() {
		return frontLogo;
	}

	/**
	 * Get the back 9 logos.
	 *
	 * @return
	 *      The back logos.
	 */
	public Image[] getBackLogo() {
		return backLogo;
	}

	public IDMask[] getFrontMask() {
		return frontMask;
	}

	public IDMask[] getBackMask() {
		return backMask;
	}

	public String getFrontLarge() {
		return frontLarge;
	}

	public String getBackLarge() {
		return backLarge;
	}

	public String[] getFrontLogoVars() {
		return frontLogoVars;
	}

	public String[] getBackLogoVars() {
		return backLogoVars; 
	}

	/**
	 * Get the status of ID Card information.
	 *
	 * @return
	 * 		true if ID card is finish loaded.
	 */
	public boolean isReady() {
		return isReady;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setDiv(String div) {
		this.div = div;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFrontTexts(String[] frontTexts) {
		this.frontTexts = frontTexts;
	}

	public void setBackTexts(String[] backTexts) {
		this.backTexts = backTexts;
	}

	public void setFrontTextModifiers(String[] frontTextModifiers) {
		this.frontTextModifiers = frontTextModifiers;
	}

	public void setBackTextModifiers(String[] backTextModifiers) {
		this.backTextModifiers = backTextModifiers;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public void setFrontMask(IDMask[] frontMask) {
		this.frontMask = frontMask;
	}

	public void setBackMask(IDMask[] backMask) {
		this.backMask = backMask;
	}

	public void setFrontLarge(String frontLarge) {
		this.frontLarge = frontLarge;
	}

	public void setBackLarge(String backLarge) {
		this.backLarge = backLarge;
	}

	public void setFrontLogoVars(String[] frontLogoVars) {
		this.frontLogoVars = frontLogoVars;
	}

	public void setBackLogoVars(String[] backLogoVars) {
		this.backLogoVars = backLogoVars;
	}

	/**
	 * Set the logos for the front.
	 *
	 * @param logos
	 * 		Logo array for the front.
	 */
	public void setFrontLogo(Image[] logos) {
		frontLogo = logos;
	}

	/**
	 * Set the logos for the back.
	 *
	 * @param logos
	 * 		Logo array for the back.
	 */
	public void setBackLogo(Image[] logos) {
		backLogo = logos;
	}

	/**
	 * Set the status of ID Card information.
	 *
	 * @param status
	 * 		boolean status.
	 */
	public void changeReadyState(boolean status) {
		this.isReady = status;
	}
	
	private String[] initStr(int size) {
	  String[] str = new String[size];
	  
	  for (int idx = 0; idx < size; idx++) {
	    str[idx] = "";
	  }
	  
	  return str;
	}
}