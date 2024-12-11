/**
 * 
 */
package com.anshu.cyns.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@Data
@RequiredArgsConstructor
public class BTNode<T> {
  @NonNull
  private final T data;
  private BTNode<T> left;
  private BTNode<T> right;
}
