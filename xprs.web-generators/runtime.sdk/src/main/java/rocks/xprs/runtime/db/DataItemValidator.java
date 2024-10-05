/*
 * The MIT License
 *
 * Copyright 2016 Ralph Borowski.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package rocks.xprs.runtime.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Abstract interface to check if a model object contains all neccessary data in the right format.
 * @author Ralph Borowski
 * @param <T> the type the validator can validate
 */
public abstract class DataItemValidator<T extends DataItem> {
  
  /**
   * Validate the given object and return a list of errors.
   * @param item the item to validate
   * @return a map of errors. The key ist the name of the attribute, the value is a list of error messages.
   */
  public abstract Map<String, List<String>> validate(T item);
  
  protected static void addError(String field, String errorMessage, Map<String, List<String>> errorList) {
    
    if (errorList.containsKey(field)) {
      errorList.get(field).add(errorMessage);
    } else {
      List<String> errorMessages = new ArrayList<>();
      errorMessages.add(errorMessage);
      errorList.put(field, errorMessages);
    }
  }
  
}
