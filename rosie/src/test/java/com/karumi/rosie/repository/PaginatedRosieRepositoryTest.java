/*
 * The MIT License (MIT) Copyright (c) 2014 karumi Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
  * do so, subject to the following conditions: The above copyright notice and this permission
  * notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE
  * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
  * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
  * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
  * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.karumi.rosie.repository;

import com.karumi.rosie.UnitTest;
import com.karumi.rosie.doubles.AnyRepositoryKey;
import com.karumi.rosie.doubles.AnyRepositoryValue;
import com.karumi.rosie.repository.datasource.PaginatedCacheDataSource;
import com.karumi.rosie.repository.datasource.PaginatedReadableDataSource;
import com.karumi.rosie.repository.policy.ReadPolicy;
import java.util.LinkedList;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PaginatedRosieRepositoryTest extends UnitTest {

  private static final int ANY_OFFSET = 0;
  private static final int ANY_LIMIT = 20;

  @Mock private PaginatedCacheDataSource<AnyRepositoryKey, AnyRepositoryValue> cacheDataSource;
  @Mock private PaginatedReadableDataSource<AnyRepositoryValue> readableDataSource;

  @Test public void shouldReturnValuesFromCacheDataSourceIfDataIsValid() throws Exception {
    PaginatedCollection<AnyRepositoryValue> cacheValues =
        givenCacheDataSourceReturnsValidValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    PaginatedCollection<AnyRepositoryValue> values = repository.getPage(ANY_OFFSET, ANY_LIMIT);

    assertEquals(cacheValues, values);
  }

  @Test public void shouldReturnItemsFromReadableDataSourceIfCacheDataSourceHasNoData()
      throws Exception {
    givenCacheDataSourceReturnsNull(ANY_OFFSET, ANY_LIMIT);
    PaginatedCollection<AnyRepositoryValue> readableValues =
        givenReadableDataSourceReturnsValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    PaginatedCollection<AnyRepositoryValue> values = repository.getPage(ANY_OFFSET, ANY_LIMIT);

    assertEquals(readableValues, values);
  }

  @Test public void shouldReturnValuesFromReadableDataSourceIfCacheDataSourceIsNotValid()
      throws Exception {
    givenCacheDataSourceReturnsNonValidValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedCollection<AnyRepositoryValue> readableValues =
        givenReadableDataSourceReturnsValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    PaginatedCollection<AnyRepositoryValue> values = repository.getPage(ANY_OFFSET, ANY_LIMIT);

    assertEquals(readableValues, values);
  }

  @Test public void shouldReturnValuesFromReadableDataSourceIfPolicyForcesOnlyReadable()
      throws Exception {
    givenCacheDataSourceReturnsValidValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedCollection<AnyRepositoryValue> readableValues =
        givenReadableDataSourceReturnsValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    PaginatedCollection<AnyRepositoryValue> values =
        repository.getPage(ANY_OFFSET, ANY_LIMIT, ReadPolicy.READABLE_ONLY);

    assertEquals(readableValues, values);
  }

  @Test public void shouldPopulateCacheDataSourceAfterGetPageFromReadableDataSource()
      throws Exception {
    givenCacheDataSourceReturnsNull(ANY_OFFSET, ANY_LIMIT);
    PaginatedCollection<AnyRepositoryValue> readableValues =
        givenReadableDataSourceReturnsValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    repository.getPage(ANY_OFFSET, ANY_LIMIT);

    verify(cacheDataSource).addOrUpdatePage(ANY_OFFSET, ANY_LIMIT, readableValues.getItems(), true);
  }

  @Test public void shouldDeleteCacheDataIfItemsAreNotValid() throws Exception {
    givenCacheDataSourceReturnsNonValidValues(ANY_OFFSET, ANY_LIMIT);
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        givenAPaginatedRepository();

    repository.getPage(ANY_OFFSET, ANY_LIMIT);

    verify(cacheDataSource).deleteAll();
  }

  private PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> givenAPaginatedRepository() {
    PaginatedRosieRepository<AnyRepositoryKey, AnyRepositoryValue> repository =
        new PaginatedRosieRepository<>();
    repository.addPaginatedCaches(cacheDataSource);
    repository.addPaginatedReadables(readableDataSource);
    return repository;
  }

  private void givenCacheDataSourceReturnsNull(int offset, int limit) throws Exception {
    when(cacheDataSource.getPage(offset, limit)).thenReturn(null);
  }

  private PaginatedCollection<AnyRepositoryValue> givenCacheDataSourceReturnsValidValues(int offset,
      int limit) throws Exception {
    return givenCacheDataSourceReturnsValues(offset, limit, true);
  }

  private PaginatedCollection<AnyRepositoryValue> givenCacheDataSourceReturnsNonValidValues(
      int offset, int limit) throws Exception {
    return givenCacheDataSourceReturnsValues(offset, limit, false);
  }

  private PaginatedCollection<AnyRepositoryValue> givenCacheDataSourceReturnsValues(int offset,
      int limit, boolean areValidValues) {
    PaginatedCollection<AnyRepositoryValue> values = getSomeValues(offset, limit);
    when(cacheDataSource.getPage(offset, limit)).thenReturn(values);
    when(cacheDataSource.isValid(any(AnyRepositoryValue.class))).thenReturn(areValidValues);
    return values;
  }

  private PaginatedCollection<AnyRepositoryValue> givenReadableDataSourceReturnsValues(int offset,
      int limit) {
    PaginatedCollection<AnyRepositoryValue> values = getSomeValues(offset, limit);
    when(readableDataSource.getPage(offset, limit)).thenReturn(values);
    return values;
  }

  private PaginatedCollection<AnyRepositoryValue> getSomeValues(int offset, int limit) {
    LinkedList<AnyRepositoryValue> values = new LinkedList<>();
    for (int i = 0; i < 10; i++) {
      values.add(new AnyRepositoryValue(new AnyRepositoryKey(i)));
    }
    PaginatedCollection<AnyRepositoryValue> paginatedValues = new PaginatedCollection<>(values);
    paginatedValues.setHasMore(true);
    paginatedValues.setOffset(offset);
    paginatedValues.setLimit(limit);
    return paginatedValues;
  }
}