package gov.lbl.glamm.client.util;

import java.util.List;

import com.google.gwt.cell.client.AbstractInputCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * Input cell containing a list box with options that vary by cell table row.
 * @author jtbates
 *
 */
public class RowDependentSelectionCell extends AbstractInputCell<String, String> {

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

	interface Template extends SafeHtmlTemplates {
		@Template("<option value=\"{0}\">{0}</option>")
		SafeHtml deselected(String option);

		@Template("<option value=\"{0}\" selected=\"selected\">{0}</option>")
		SafeHtml selected(String option);
	}

	private static Template template;
	
	private String cellWidth = null;

	/**
	 * Constructor
	 */
	public RowDependentSelectionCell() {
		this(null);
	}
	
	/**
	 * Constructor allowing the specification of the cell's width.
	 * @param cellWidth The cell's width.
	 */
	public RowDependentSelectionCell(String cellWidth) {
		super("change");
		this.cellWidth = cellWidth;
		if (template == null) {
			template = GWT.create(Template.class);
		}
	}

	@Override
	public void onBrowserEvent(Context context, Element parent, String value,
			NativeEvent event, ValueUpdater<String> valueUpdater) {
		super.onBrowserEvent(context, parent, value, event, valueUpdater);
		String type = event.getType();
		if ("change".equals(type)) {
			HasOptions key = (HasOptions) context.getKey();
			SelectElement select = parent.getFirstChild().cast();
			String newValue = null;
			if(key.hasOptions()) {
				List<String> options = key.getOptions();
				int optionsPreambleSize = key.getOptionsPreamble() == null ? 0 : key.getOptionsPreamble().size();
				int selectedOptionIndex = select.getSelectedIndex() - optionsPreambleSize;
				if(selectedOptionIndex >= 0)
					newValue = options.get(selectedOptionIndex);
			}
			else
				newValue = key.getNoOptionsString();
			setViewData(key, newValue);
			finishEditing(parent, newValue, key, valueUpdater);
			if (valueUpdater != null) {
				valueUpdater.update(newValue);
			}
		}
	}

	@Override
	public void render(Context context, String value, SafeHtmlBuilder sb) {
		// Get the view data.
		HasOptions key = (HasOptions) context.getKey();
		String viewData = getViewData(key);
		if (viewData != null && viewData.equals(value)) {
			clearViewData(key);
			viewData = null;
		}

		
		if(key.hasOptions()) {
			if(cellWidth != null)
				sb.appendHtmlConstant("<select tabindex=\"-1\" style=\"width: " + cellWidth + "\">");
			else
				sb.appendHtmlConstant("<select tabindex=\"-1\">");
			
			int selectedOptionIndex = getSelectedOptionIndex(key, viewData == null ? value : viewData);

			int index = 0;
			
			// append options preamble
			List<String> optionsPreamble = key.getOptionsPreamble();
			if(optionsPreamble != null) {
				for(String option : optionsPreamble)
					sb.append(template.deselected(option));
			}
			
			// append the rest of the options
			for (String option : key.getOptions()) {
				if (index++ == selectedOptionIndex) {
					sb.append(template.selected(option));
				} else {
					sb.append(template.deselected(option));
				}
			}
			sb.appendHtmlConstant("</select>");
		}
		else
			sb.appendHtmlConstant("<div>" + key.getNoOptionsString() + "</div>");
		
	}

	private int getSelectedOptionIndex(HasOptions key, String value) {
		if(key.hasOptions()) {
			int index = 0;
			for(String option : key.getOptions()) {
				if(option.equals(value))
					return index;
				index++;
			}
		}
		return -1;
	}
}
