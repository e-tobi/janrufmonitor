package de.janrufmonitor.ui.jface.application.editor.rendering;

import de.janrufmonitor.framework.ICall;
import de.janrufmonitor.framework.ICaller;
import de.janrufmonitor.framework.IJAMConst;
import de.janrufmonitor.framework.i18n.II18nManager;
import de.janrufmonitor.runtime.PIMRuntime;
import de.janrufmonitor.ui.jface.application.rendering.AbstractTableCellRenderer;
import de.janrufmonitor.ui.jface.application.rendering.IEditorCellRenderer;
import de.janrufmonitor.ui.jface.application.rendering.IJournalCellRenderer;
import de.janrufmonitor.util.io.ImageHandler;

public class Image extends AbstractTableCellRenderer implements IJournalCellRenderer, IEditorCellRenderer {

	private static String NAMESPACE = "ui.jface.application.editor.rendering.Image";

	private II18nManager m_i18n;
	private String m_language;
	private String m_i18nData;

	public String renderAsText() {
		if (this.m_o!=null) {
			if (this.m_o instanceof ICall) {
				this.m_o = ((ICall)this.m_o).getCaller();
			}
			if (this.m_o instanceof ICaller) {
				if (ImageHandler.getInstance().hasImage(((ICaller)this.m_o))) {          		
					return this.getI18nYes();
				}
			}
		}
		return "";
	}
	
	private String getI18nYes() {
		if (this.m_i18nData==null)
			this.m_i18nData = this.getI18nManager().getString(getNamespace(), "yes", "label", this.getLanguage());
			
		return this.m_i18nData;
	}

	public String getID() {
		return "Image".toLowerCase();
	}
	
	private II18nManager getI18nManager() {
		if (this.m_i18n==null) {
			this.m_i18n = PIMRuntime.getInstance().getI18nManagerFactory().getI18nManager();
		}
		return this.m_i18n;
	}
	
	private String getLanguage() {
		if (this.m_language==null) {
			this.m_language = 
				PIMRuntime.getInstance().getConfigManagerFactory().getConfigManager().getProperty(
					IJAMConst.GLOBAL_NAMESPACE,
					IJAMConst.GLOBAL_LANGUAGE
				);
		}
		return this.m_language;
	}

	public String getNamespace() {
		return NAMESPACE;
	}
	
}
