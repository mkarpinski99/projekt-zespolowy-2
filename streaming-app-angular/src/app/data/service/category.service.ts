import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { Observable } from 'rxjs';
import { Category } from '../schema/category';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  constructor(
    private _api: ApiService
  ) { }

  getAllCategories(): Observable<Array<Category>> {
    return this._api.get('categories')
  }

  // get(categoryId: number): Observable<Category> {
  //   let params = new HttpParams();
  //   if (categoryId)
  //     params = params.set('id', categoryId);
  //   return this.api.get('category', params);
  // }
}
