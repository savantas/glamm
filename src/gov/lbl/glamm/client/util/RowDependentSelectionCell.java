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

public class RowDependentSelectionCell extends AbstractInputCell<String, String> {

	public interface HasOptions {
		public String getNoOptionsString();
		public List<String> getOptions();
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

	public RowDependentSelectionCell() {
		this(null);
	}
	
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
				newValue = options.get(select.getSelectedIndex());
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
			
			int selectedIndex = getSelectedIndex(key, viewData == null ? value : viewData);

			int index = 0;
			sb.append(template.deselected(Integer.toString(key.getOptions().size()) + " candidates"));
			sb.append(template.deselected("-"));
			for (String option : key.getOptions()) {
				if (index++ == selectedIndex) {
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

	private int getSelectedIndex(HasOptions key, String value) {
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
