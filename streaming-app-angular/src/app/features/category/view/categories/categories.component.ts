import { Component, OnInit } from '@angular/core';
import { CategoriesListComponent } from '@features/category/components/categories-list/categories-list.component';
import { Category } from 'app/data/schema/category';
import { CategoryService } from 'app/data/service/category.service';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [CategoriesListComponent],
  templateUrl: './categories.component.html',
  styleUrl: './categories.component.scss'
})
export class CategoriesComponent implements OnInit{
  categories: Array<Category> = [];
  contentLoading: boolean = true;

  constructor(
    private _categoryService: CategoryService
  ) {}

  ngOnInit() {
    this.fetchCategories();
  }

  fetchCategories() {
    this.contentLoading = true;
    this._categoryService.getAllCategories().subscribe({
      next: result => this.categories = result,
      error: console.error,
      complete: () => this.contentLoading = false
    });
  }
}
