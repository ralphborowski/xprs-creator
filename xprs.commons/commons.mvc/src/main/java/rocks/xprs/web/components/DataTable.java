/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocks.xprs.components;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import rocks.xprs.db.Entity;
import rocks.xprs.db.ResultList;
import rocks.xprs.types.Monetary;
import rocks.xprs.util.MultiValuedMap;
import rocks.xprs.util.ReflectiveUtil;
import rocks.xprs.util.Strings;
import rocks.xprs.web.Context;
import rocks.xprs.web.UrlBuilder;

/**
 *
 * @author borowski
 * @param <T> the type of the list items
 */
public class DataTable<T extends Entity> extends GroupView implements FormControl {

  private static final Logger LOG = Logger.getLogger(DataTable.class.getName());

  private UpdateType updateType = UpdateType.POST;

  private List<String> columns;
  private final Map<Long, T> itemsMap = new HashMap<>();
  private final List<T> selectedItems = new LinkedList<>();
  private boolean isCheckboxesVisible = false;
  private boolean isPagerVisible = true;
  private String pagerPrefix = "";
  private CellRenderer<T> renderer;

  private Class<T> type;
  private int limit;
  private int offset;
  private int total;

  private String hint;

  private final List<Checkbox<Long>> checkboxes = new LinkedList<>();

  @Override
  public void initViews() {

    super.initViews();

    // init renderer with default
    if (renderer == null) {
      renderer = new DefaultViewCellRenderer(type);
    }

    addDecorator(new CssClassDecorator().add("data-table"));

    // create table
    Table table = new Table();
    addView(table);

    // create table header
    TableHead tableHead = new TableHead();
    table.addView(tableHead);
    TableRow headerRow = new TableRow();
    tableHead.addView(headerRow);

    // put checkboxes in front of line?
    if (isCheckboxesVisible) {
      headerRow.addView(new Cell()
              .setCellType(Cell.CellType.HEADING)
              .addView(getName() + ".select-all", new Checkbox(Boolean.class)
                      .bind(false, null)
                      .addDecorator(new CssClassDecorator()
                              .add("data-table-select-all"))));
    }

    // print headers
    for (String c : columns) {
      headerRow.addView(new Cell()
              .setCellType(Cell.CellType.HEADING)
              .addView(renderer.renderHeader(this, c))
      );
    }

    // create table body
    TableBody tableBody = new TableBody();
    table.addView(tableBody);
    List<T> items = (List<T>) getModel();
    for (int i = 0; i < items.size(); i++) {
      tableBody.addView(renderRow(items.get(i), i));
    }

    // show pager?
    if (isPagerVisible && (this.total > this.limit)) {

      int pages = (int) Math.ceil((double) this.total / (double) this.limit);
      int currentPage = (this.offset / this.limit) + 1;

      ListView pager = new ListView();
      pager.addDecorator(new CssClassDecorator().add("pager"));
      addView(pager);

      for (int i = 1; i <= pages; i++) {
        Link link = new Link().setLabel(String.valueOf(i));
        if (i == currentPage) {
          link.addDecorator(new CssClassDecorator().add("pager-selected"));
        }

        // create URL with new parameters
        MultiValuedMap<String, String> newParameters = new MultiValuedMap<>();
        newParameters.putAll(getContext().getGetParameters());
        newParameters.setValue(DataTable.prefixString(pagerPrefix, "offset"),
                String.valueOf((i - 1) * this.limit));
        newParameters.setValue(DataTable.prefixString(pagerPrefix, "limit"),
                String.valueOf(this.limit));
        link.setUrl(UrlBuilder.buildGet(getContext().getRequest(), newParameters));

        pager.addView(link);
      }
    }
  }

  private TableRow renderRow(T item, int index) {
    TableRow row = new TableRow();
    if (isCheckboxesVisible) {
      Checkbox checkbox = new Checkbox(Long.class).bind(item, "id");
      checkboxes.add(checkbox);
      row.addView(new Cell()
              .setCellType(Cell.CellType.HEADING)
              .addView(getName() + ".id", checkbox));
    }

    for (String c : columns) {
      Cell cell = renderer.renderCell(row, item, c, index);
      if (cell != null) {
        row.addView(cell);
      }
    }
    return row;
  }

  public static int getOffset(Context context, String pagerPrefix) {
    String offset = context.getGetParameters().getValue(
            DataTable.prefixString(pagerPrefix, "offset"));
    if (offset != null) {
      try {
        return Integer.parseInt(offset);
      } catch (NumberFormatException ex) {
        // ignore
      }
    }
    return 0;
  }

  public static int getLimit(Context context, String pagerPrefix) {
    String limit = context.getGetParameters().getValue(
            DataTable.prefixString(pagerPrefix, "limit"));
    if (limit != null) {
      try {
        return Integer.parseInt(limit);
      } catch (NumberFormatException ex) {
        // ignore
      }
    }
    return 50;
  }

  private static String prefixString(String prefix, String string) {
    if (Strings.isEmpty(prefix)) {
      return string;
    }
    return prefix + "." + string;
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  @Override
  public DataTable<T> bind(String expression) {
    super.bind(expression);
    return this;
  }

  @Override
  public DataTable<T> bind(Object model, String expression) {
    if (model instanceof List) {
      List list = (List) model;
      if (!list.isEmpty()) {
        bind(list, list.get(0).getClass());
        return this;
      }
    }
    bind(null, Object.class);
    return this;
  }

  public DataTable<T> bind(List<T> model, Class type) {
    bind(new ResultList<>(model, type));
    return this;
  }

  public DataTable<T> bind(ResultList<T> model) {
    itemsMap.clear();
    for (T item : model) {
      itemsMap.put(item.getId(), item);
    }
    this.limit = model.getLimit();
    this.offset = model.getOffset();
    this.total = model.getTotal();
    this.type = model.getType();

    super.bind(model, null);
    return this;
  }

  @Override
  public DataTable<T> unbind() {
    super.unbind();
    return this;
  }

  public DataTable<T> setColumns(List<String> columns) {
    this.columns = columns;
    return this;
  }

  public DataTable<T> setColumns(String... columns) {
    this.columns = Arrays.asList(columns);
    return this;
  }

  public DataTable<T> setCheckboxesVisible(boolean isCheckboxesVisible) {
    this.isCheckboxesVisible = isCheckboxesVisible;
    return this;
  }

  public DataTable<T> setPagerVisible(boolean isPagerVisible) {
    this.isPagerVisible = isPagerVisible;
    return this;
  }

  public String getPagerPrefix() {
    return pagerPrefix;
  }

  public DataTable<T> setPagerPrefix(String pagerPrefix) {
    this.pagerPrefix = pagerPrefix;
    return this;
  }

  public DataTable<T> setRenderer(CellRenderer<T> renderer) {
    this.renderer = renderer;
    return this;
  }

  @Override
  public DataTable<T> addDecorator(Decorator decorator) {
    super.addDecorator(decorator);
    return this;
  }

  @Override
  public <U extends View> DataTable<T> addDecorator(Decorator decorator, Class<U> clazz) {
    super.addDecorator(decorator, clazz);
    return this;
  }

  @Override
  public DataTable<T> addDecorator(Decorator... decorator) {
    super.addDecorator(decorator);
    return this;
  }
  //</editor-fold>

  @Override
  public DataTable updateOn(UpdateType updateType) {
    this.updateType = updateType;
    return this;
  }

  @Override
  public void updateModel() {
    if ((updateType == UpdateType.POST && !getContext().isPostback())
            || (updateType == UpdateType.GET && getContext().isPostback())) {

      return;
    }

    selectedItems.clear();
    for (Checkbox<Long> c : checkboxes) {
      if (!c.getValue().equals("") && c.isChecked() && itemsMap.containsKey(c.getValue())) {
        selectedItems.add(itemsMap.get(c.getValue()));
      }
    }
  }

  public List<T> getSelectedItems() {
    return selectedItems;
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public void addValidator(Validator v) {
    // ignore
  }

  @Override
  public void removeValidator(Validator v) {
    // ignore
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public void addError(String errorMessage) {
    // ignore
  }

  @Override
  public List<String> getErrors() {
    return Collections.EMPTY_LIST;
  }

  @Override
  public UpdateType getUpdateType() {
    return this.updateType;
  }

  @Override
  public DataTable<T> setHint(String hint) {
    this.hint = hint;
    return this;
  }

  public static interface CellRenderer<T extends Entity> {

    public View renderHeader(View parent, String field);

    public Cell renderCell(View parent, T entity, String field, int sequence);

  }

  public static class DefaultViewCellRenderer<T extends Entity> implements CellRenderer<T> {

    private final Class<T> entityClass;

    public DefaultViewCellRenderer(Class<T> entityClass) {
      this.entityClass = entityClass;
    }

    @Override
    public View renderHeader(View parent, String field) {
      return new TextView().setText(parent.t(
              Strings.decapitalize(entityClass.getSimpleName()) + ".attributes." + field));
    }

    @Override
    public Cell renderCell(View parent, T entity, String field, int sequence) {
      Method getter = ReflectiveUtil.getGetterMethod(entity.getClass(), field);
      if (getter != null && getter.getReturnType().isEnum()) {
        Object value = ReflectiveUtil.getValue(entity, field, 0);
        return new Cell().addView(new TextView()
                .setText(parent.t(Strings.decapitalize(value.getClass().getSimpleName())
                        + ".options." + String.valueOf(value))));
      }
      return new Cell().addView(new TextView().bind(entity, field));
    }
  }

  public static abstract class DefaultLinkCellRenderer<T extends Entity> implements CellRenderer<T> {

    private final Class<T> entityClass;

    public DefaultLinkCellRenderer(Class<T> entityClass) {
      this.entityClass = entityClass;
    }

    public abstract String getLink(Context context, T entity);

    @Override
    public View renderHeader(View parent, String field) {
      return new TextView().setText(parent.t(
              Strings.decapitalize(entityClass.getSimpleName()) + ".attributes." + field));
    }

    @Override
    public Cell renderCell(View parent, T entity, String field, int sequence) {
      Method getter = ReflectiveUtil.getGetterMethod(entity.getClass(), field);
      if (getter != null && getter.getReturnType().isEnum()) {
        Object value = ReflectiveUtil.getValue(entity, field, 0);
        return new Cell().addView(new Link()
                .setUrl(getLink(parent.getContext(), entity))
                .setLabel(parent.t(Strings.decapitalize(value.getClass().getSimpleName())
                        + ".options." + String.valueOf(value))));
      }
      return new Cell().addView(new Link()
              .setUrl(getLink(parent.getContext(), entity)).bind(entity, field));
    }
  }

  public static class DefaultEditCellRenderer<T extends Entity> implements DataTable.CellRenderer<T> {

    private final Class<T> entityClass;
    private final Map<String, List<? extends Entity>> domain;

    public DefaultEditCellRenderer(Class<T> entityClass, Map<String, List<? extends Entity>> domain) {

      this.entityClass = entityClass;
      if (domain != null) {
        this.domain = domain;
      } else {
        this.domain = Collections.EMPTY_MAP;
      }
    }

    @Override
    public View renderHeader(View parent, String field) {
      return new TextView().setText(parent.t(
              Strings.decapitalize(entityClass.getSimpleName()) + ".attributes." + field));
    }

    @Override
    public Cell renderCell(View parent, T entity, String field, int sequence) {
      Class returnType = ReflectiveUtil.getGetterMethod(entityClass, field).getReturnType();

      Cell cell = new Cell();
      if (Entity.class.isAssignableFrom(returnType)) {
        cell.addView(new Select(returnType)
                .setOptionRenderer(new EntityOptionRenderer(domain.get(field), parent.getContext()))
                .setRenderEmpty(true)
                .bind(entity, field));
      } else if (Monetary.class.isAssignableFrom(returnType)) {
        cell.addView(new MonetaryInputView().bind(entity, field));
      } else if (returnType.isEnum()) {
        cell.addView(new Select(returnType)
                .setOptionRenderer(new EnumOptionRenderer(returnType))
                .setRenderEmpty(true)
                .bind(entity, field));
      } else if (Boolean.class.equals(returnType)) {
        cell.addView(new Checkbox<>(Boolean.class)
                    .bind(entity, field));
      } else {
        cell.addView(new TextInput(returnType).bind(entity, field));
      }

      return cell;
    }
  }
}
