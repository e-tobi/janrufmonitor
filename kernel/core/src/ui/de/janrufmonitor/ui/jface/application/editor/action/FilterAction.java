package de.janrufmonitor.ui.jface.application.editor.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import de.janrufmonitor.framework.IJAMConst;
import de.janrufmonitor.repository.filter.AttributeFilter;
import de.janrufmonitor.repository.filter.IFilter;
import de.janrufmonitor.runtime.IRuntime;
import de.janrufmonitor.runtime.PIMRuntime;
import de.janrufmonitor.ui.jface.application.AbstractAction;
import de.janrufmonitor.ui.jface.application.ApplicationImageDescriptor;
import de.janrufmonitor.ui.jface.AbstractTableLabelProvider;
import de.janrufmonitor.ui.jface.application.editor.Editor;
import de.janrufmonitor.ui.jface.application.editor.EditorConfigConst;
import de.janrufmonitor.ui.jface.application.editor.EditorFilterManager;
import de.janrufmonitor.ui.jface.wizards.EditorFilterWizard;
import de.janrufmonitor.ui.swt.DisplayManager;
import de.janrufmonitor.ui.swt.SWTExecuter;
import de.janrufmonitor.ui.swt.SWTImageManager;

public class FilterAction extends AbstractAction implements EditorConfigConst {

	private class FilterContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object o) {
			List l = (List)o;
			return l.toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {	
		}
		
	}
	
	private class FilterViewerSorter extends ViewerComparator {

		public int compare(Viewer v, Object o1, Object o2) {
			if (o1 instanceof IFilter[] && o2 instanceof IFilter[] ) {
				EditorFilterManager efm = new EditorFilterManager();
				return efm.getFiltersToLabelText((IFilter[]) o1, 45).compareToIgnoreCase(efm.getFiltersToLabelText((IFilter[]) o2, 45));
			}		
			return super.compare(v, o1, o2);
		}
		
	}
	
	private class FilterLabelProvider extends AbstractTableLabelProvider {
		public String getColumnText(Object o, int column) {
			IFilter[] f = (IFilter[])o;
			
		    switch (column) {
		    case 0:
		      return new EditorFilterManager().getFiltersToLabelText(f, 45);
		    }
			return null;
		}
	}
	
	private class FilterDialog extends TitleAreaDialog {		
		
		private IFilter[] m_filters;
		private TableViewer tv; 
		private List f_l;
			
		public FilterDialog(Shell shell) {
			super(shell);
		}
		
//		public void setFilters(IFilter[] filters) {
//			this.m_filters = filters;
//		}
//		
//		public IFilter[] getFilters() {
//			return this.m_filters;
//		}
		
		protected Control createContents(Composite parent) {
			Control c = super.createContents(parent);
			
			setTitle(
				getI18nManager().getString(
					getNamespace(),
					"dialogtitle",
					"label",
					getLanguage()
				)
			);
			setMessage(getI18nManager().getString(
					getNamespace(),
					"dialogtitle",
					"description",
					getLanguage()
				));
			return c;
		}

		protected Control createDialogArea(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(1, false));
			
			Composite view = new Composite(composite, SWT.NONE);
			view.setLayout(new GridLayout(2, false));

			tv = new TableViewer(view, SWT.BORDER | SWT.V_SCROLL);
			tv.getTable().setLinesVisible(false);
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.heightHint = 80;
			gd.widthHint = 250;
			tv.getTable().setLayoutData(gd);
			tv.setContentProvider(new FilterContentProvider());
			tv.setLabelProvider(new FilterLabelProvider());
			f_l = this.getContentAsList();
			tv.setInput(f_l);
			tv.setComparator(new FilterViewerSorter());
			
			tv.getTable().addMouseListener(
				new MouseAdapter() {
					public void mouseDoubleClick(MouseEvent e) {
						StructuredSelection s = (StructuredSelection) tv.getSelection();
						if (!s.isEmpty()) {
							IFilter[] filter = (IFilter[]) s.getFirstElement();
							if (filter!=null && filter.length>0){
								for (int i=0;i<filter.length;i++) {
									if (filter[i] instanceof AttributeFilter) {
										new SWTExecuter(true, "InfoDialog") {
											protected void execute() {
												MessageDialog
												.openInformation(
														new Shell(
																DisplayManager
																		.getDefaultDisplay()),
														getI18nManager()
																.getString(
																		getNamespace(),
																		"wrongtype",
																		"label",
																		getLanguage()),
														getI18nManager()
																.getString(
																		getNamespace(),
																		"wrongtype",
																		"description",
																		getLanguage()));
											}
										}.start();
										return;
									}
								}
								openFilterWizard(filter);	
							}													
						}
					}
				}
			);
			
			Composite buttonarea = new Composite(view, SWT.NONE);
			buttonarea.setLayout(new GridLayout(1, false));

			gd = new GridData(GridData.FILL_BOTH);
			gd.widthHint = 80;
			
			new Label(buttonarea, SWT.NONE);
			
			Button add = new Button(buttonarea, SWT.PUSH);
			add.setText(
				getI18nManager().getString(
						getNamespace(),
						"add",
						"label",
						getLanguage()
					)					
				);
			add.setLayoutData(gd);
			add.addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						openFilterWizard(null);						
					}
				}	
			);
			
			Button edit = new Button(buttonarea, SWT.PUSH);
			edit.setText(
				getI18nManager().getString(
						getNamespace(),
						"edit",
						"label",
						getLanguage()
					)					
				);
			edit.setLayoutData(gd);
			edit.addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						StructuredSelection s = (StructuredSelection) tv.getSelection();
						if (!s.isEmpty()) {
							IFilter[] filter = (IFilter[]) s.getFirstElement();
							if (filter!=null && filter.length>0)
								openFilterWizard(filter);						
						}
					}
				}	
			);			
			
			Button remove = new Button(buttonarea, SWT.PUSH);
			remove.setText(
				getI18nManager().getString(
						getNamespace(),
						"remove",
						"label",
						getLanguage()
					)					
				);
			remove.setLayoutData(gd);
			remove.addSelectionListener(
				new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						IStructuredSelection s = (IStructuredSelection)tv.getSelection();
						if (!s.isEmpty()) {
							m_filters = (IFilter[]) s.getFirstElement();
							if (m_filters!=null && m_filters.length>0)
								f_l.remove(m_filters);
							tv.setInput(f_l);
						}
					}
				}			
			);
			
			return super.createDialogArea(parent);
		}
		
		private List getContentAsList() {
			List l = new ArrayList();
			
			EditorFilterManager jfm = new EditorFilterManager();
			Properties c = m_app.getApplication().getConfiguration();
			Iterator iter = c.keySet().iterator();
			String key = null;
			while (iter.hasNext()) {
				key = (String)iter.next();
				if (key.startsWith("filter_")) {
					String filter = c.getProperty(key);
					l.add(jfm.getFiltersFromString(filter));
				}
			}
			return l;
		}
		
		private void removeFitersFromConfig(){
			List l = new ArrayList();
			Properties c = m_app.getApplication().getConfiguration();
			Iterator iter = c.keySet().iterator();
			String key = null;
			while (iter.hasNext()) {
				key = (String)iter.next();
				if (key.startsWith("filter_")) {
					l.add(key);
				}
			}
			iter = null;
			for (int i=0;i<l.size();i++) {
				m_app.getApplication().getConfiguration().remove(l.get(i));
				getRuntime().getConfigManagerFactory().getConfigManager().removeProperty(Editor.NAMESPACE, (String)l.get(i));
			}
			getRuntime().getConfigManagerFactory().getConfigManager().saveConfiguration();
		}
		
		private void openFilterWizard(IFilter[] filters) {
		    Display display = DisplayManager.getDefaultDisplay();
			Shell shell = new Shell(display);

		    WizardDialog.setDefaultImage(SWTImageManager.getInstance(PIMRuntime.getInstance()).get(IJAMConst.IMAGE_KEY_PIM_ICON));
		    EditorFilterWizard filterWiz = new EditorFilterWizard(filters);
		    WizardDialog dlg = new WizardDialog(shell, filterWiz);
		    dlg.open();
		    if (dlg.getReturnCode() == WizardDialog.OK) {
		    	IFilter[] newFilters = filterWiz.getResult();
		    	if (filters!=null)
		    		this.f_l.remove(filters);
		    	
		    	if (newFilters!=null && newFilters.length>0)
		    		this.f_l.add(newFilters);
		    }
		    tv.setInput(this.f_l);
		}
		
		protected void okPressed() {
			if (f_l!=null) {
				EditorFilterManager jfm = new EditorFilterManager();
				this.removeFitersFromConfig();
				for (int i=0;i<f_l.size();i++) {
					IFilter[] f = (IFilter[])f_l.get(i);
					if (f!=null && f.length>0)
					m_app.getApplication().getConfiguration().setProperty(
						"filter_"+i,
						jfm.getFiltersToString(f)
					);
				}
			}
			super.okPressed();
		}
		
	}
	
	private static String NAMESPACE = "ui.jface.application.editor.action.FilterAction";
	
	private IRuntime m_runtime;

	public FilterAction() {
		super();
		this.setText(
			this.getI18nManager().getString(
				this.getNamespace(),
				"title",
				"label",
				this.getLanguage()
			)
		);
		this.setImageDescriptor(new ApplicationImageDescriptor(
			SWTImageManager.getInstance(this.getRuntime()).getImagePath(IJAMConst.IMAGE_KEY_FILTER_GIF)
		));			
	}
	
	public IRuntime getRuntime() {
		if (this.m_runtime==null) {
			this.m_runtime = PIMRuntime.getInstance();
		}
		return this.m_runtime;
	}

	public String getID() {
		return "editor_filter";
	}

	public String getNamespace() {
		return NAMESPACE;
	}

	public void run() {
		FilterDialog fd = new FilterDialog(
			new Shell(DisplayManager.getDefaultDisplay())
		);
		
		//EditorFilterManager jfm = new EditorFilterManager();

		fd.setBlockOnOpen(true);
		int result = fd.open();
		if (result==FilterDialog.OK) {
			this.m_app.updateViews(true);
		}
	}
}
