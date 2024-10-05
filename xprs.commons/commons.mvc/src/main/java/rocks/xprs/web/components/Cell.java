package rocks.xprs.components;

import rocks.xprs.html.HtmlElement;
import rocks.xprs.html.HtmlNode;

/**
 *
 * @author Ralph Borowski <ralph.borowski@borowski.it>
 */
public class Cell extends GroupView {

  public enum CellType {CELL, HEADING}

  private CellType cellType = CellType.CELL;
  private Integer colSpan = null;
  private Integer rowSpan = null;

  public Cell setCellType(CellType type) {
    this.cellType = type;
    return this;
  }

  @Override
  public Cell addView(View view) {
    super.addView(view);
    return this;
  }

  @Override
  public Cell addView(String name, View view) {
    super.addView(name, view);
    return this;
  }

  @Override
  public Cell removeView(View view) {
    super.removeView(view);
    return this;
  }

  @Override
  public HtmlNode render() {
    HtmlElement baseElement = new HtmlElement(cellType == CellType.HEADING ? "th" : "td");

    if (colSpan != null) {
      baseElement.setAttribute("colspan", String.valueOf(colSpan));
    }

    if (rowSpan != null) {
      baseElement.setAttribute("rowspan", String.valueOf(rowSpan));
    }

    for (View v : childViews) {
      baseElement.addChildNode(v.render());
    }
    return applyDecorators(baseElement);
  }

  //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
  /**
   * @return the colSpan
   */
  public Integer getColSpan() {
    return colSpan;
  }

  /**
   * @param colSpan the colSpan to set
   * @return the current view
   */
  public Cell setColSpan(Integer colSpan) {
    this.colSpan = colSpan;
    return this;
  }

  /**
   * @return the rowSpan
   */
  public Integer getRowSpan() {
    return rowSpan;
  }

  /**
   * @param rowSpan the rowSpan to set
   * @return the current view
   */
  public Cell setRowSpan(Integer rowSpan) {
    this.rowSpan = rowSpan;
    return this;
  }
  //</editor-fold>

}
