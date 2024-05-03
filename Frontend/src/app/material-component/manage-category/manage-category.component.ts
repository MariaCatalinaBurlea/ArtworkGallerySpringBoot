import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { CategoryService } from 'src/app/services/category.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { CategoryComponent } from '../dialog/category/category.component';
import { ConfirmationComponent } from '../dialog/confirmation/confirmation.component';
import { AuthService } from 'src/app/services/auth.service';
import { Category } from 'src/app/model/category.model';

@Component({
  selector: 'app-manage-category',
  templateUrl: './manage-category.component.html',
  styleUrls: ['./manage-category.component.scss'],
})
export class ManageCategoryComponent implements OnInit {
  displayedColumnsForUsers: string[] = ['id', 'name'];
  displayedColumnsForAdmins: string[] = ['id', 'name', 'edit', 'delete'];
  displayedColumns: string[] = [];

  dataSource: any;
  responseMessage: any;

  categoryId: string = '';

  constructor(
    private categoryService: CategoryService,
    private ngxService: NgxUiLoaderService,
    private dialog: MatDialog,
    private snackbarService: SnackbarService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.ngxService.start();
    this.createTableData();
    if (this.isAdmin()) {
      this.displayedColumns = this.displayedColumnsForAdmins;
    } else {
      this.displayedColumns = this.displayedColumnsForUsers;
    }
  }

  createTableData(): void {
    this.categoryService.getAllCategories().subscribe(
      (categories: Category[]) => {
        this.ngxService.stop();
        this.dataSource = new MatTableDataSource(categories);
      },
      (error: any) => {
        this.ngxService.stop();
        if (error.error) {
          console.log(error.error);
          this.responseMessage = error.error;
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snackbarService.openSnackBar(this.responseMessage, 'error');
      }
    );
  }

  applyFilter(event: Event) {
    const filerValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filerValue.trim().toLowerCase();
  }

  getCategoriesById() {
    if (this.categoryId === '') {
      this.createTableData();
      return;
    }

    let id = Number(this.categoryId);
    if (isNaN(id)) {
      this.snackbarService.openSnackBar('The input must be a number!', 'error');
      return;
    }

    this.categoryService.getCategoryById(id).subscribe(
      (category: Category) => {
        this.ngxService.stop();
        this.dataSource = new MatTableDataSource([category]);
      },
      (error: any) => {
        this.ngxService.stop();
        if (error.error) {
          console.log(error.error);
          this.responseMessage = error.error;
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snackbarService.openSnackBar(this.responseMessage, 'error');
      }
    );
  }

  handleAddAction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Add',
    };

    dialogConfig.width = '550px';
    const dialogRef = this.dialog.open(CategoryComponent, dialogConfig);
    this.router.events.subscribe(() => {
      dialogRef.close();
    });

    const sub = dialogRef.componentInstance.onAddCategory.subscribe(
      (response) => {
        this.createTableData();
      }
    );
  }

  handleEditAction(data: any) {
    console.log('edit pressed');
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Edit',
      data: data,
    };
    dialogConfig.width = '550px';
    const dialogRef = this.dialog.open(CategoryComponent, dialogConfig);
    this.router.events.subscribe(() => {
      dialogRef.close();
    });
    const sub = dialogRef.componentInstance.onEditCategory.subscribe(
      (response) => {
        this.createTableData();
      }
    );
  }

  handleDeleteAction(values: any) {
    console.log('delete pressed');
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      message: 'delete ' + values.name + ' category',
      confirmation: true,
    };
    const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe(
      (response) => {
        this.ngxService.start();
        this.deleteCategory(values.id);
        dialogRef.close();
      }
    );
  }

  private deleteCategory(id: any) {
    this.categoryService.deleteCategory(id).subscribe(
      (response: any) => {
        this.ngxService.stop();
        this.createTableData();
        this.responseMessage = response?.message;
        this.snackbarService.openSnackBar(this.responseMessage, 'Success');
      },
      (error: any) => {
        this.ngxService.stop();
        if (error.error) {
          console.log(error.error);
          this.responseMessage = error.error;
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snackbarService.openSnackBar(this.responseMessage, 'error');
      }
    );
  }

  isAdmin(): boolean {
    return this.authService.isAdmin();
  }
}
