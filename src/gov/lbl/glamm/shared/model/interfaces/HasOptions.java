package gov.lbl.glamm.shared.model.interfaces;

import java.util.List;

/**
 * Interface describing how options are displayed in a RowDependentSelectionCell.
 * @author jtbates
 *
 */
public interface HasOptions {
	/**
	 * Gets the string to be displayed when no options are available.
	 * @return The string.
	 */
	public String getNoOptionsString();
	
	/**
	 * Gets the list of selectable options.
	 * @return The list of options.
	 */
	public List<String> getOptions();
	
	/**
	 * Gets the list of options displayed first in the selection cell.  Generally, these won't do anything - may be empty.
	 * @return The list of preamble options.
	 */
	public List<String> getOptionsPreamble();
	
	/**
	 * Indicates whether or not options are available.
	 * @return Flag indicating whether or not options are available.
	 */
	public boolean hasOptions();
}
