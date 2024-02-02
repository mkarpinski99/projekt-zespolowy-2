import { Component, Input } from '@angular/core';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterModule } from '@angular/router';
import { Category } from 'app/data/schema/category';

@Component({
  selector: 'app-categories-list',
  standalone: true,
  imports: [RouterModule, MatProgressSpinnerModule],
  templateUrl: './categories-list.component.html',
  styleUrl: './categories-list.component.scss'
})
export class CategoriesListComponent {
  @Input()
  categories: Array<Category> = []
  @Input()
  loading: boolean = true;
}
