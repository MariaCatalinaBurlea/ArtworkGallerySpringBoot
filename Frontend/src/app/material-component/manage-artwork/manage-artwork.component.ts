import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { ArtworkService } from 'src/app/services/artwork.service';
import { AuthService } from 'src/app/services/auth.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { ConfirmationComponent } from '../dialog/confirmation/confirmation.component';
import { ArtworkComponent } from '../dialog/artwork/artwork.component';
import { CategoryService } from 'src/app/services/category.service';
import { Artwork } from 'src/app/model/artwork.model';
import { Category } from 'src/app/model/category.model';

@Component({
  selector: 'app-manage-artwork',
  templateUrl: './manage-artwork.component.html',
  styleUrls: ['./manage-artwork.component.scss'],
})
export class ManageArtworkComponent implements OnInit {
  displayedColumnsForUsers: string[] = [
    'id',
    'title',
    'description',
    'price',
    'size',
    'status',
    'categoryId',
    'categoryName',
  ];
  displayedColumnsForAdmins: string[] = [
    'id',
    'title',
    'description',
    'price',
    'categoryId',
    'categoryName',
    'edit',
    'delete',
  ];
  displayedColumns: string[] = [];

  dataSource: any;
  responseMessage: any;

  categories: Category[] = [];

  constructor(
    private artworkService: ArtworkService,
    private ngxService: NgxUiLoaderService,
    private dialog: MatDialog,
    private snackbarService: SnackbarService,
    private authService: AuthService,
    private categoryService: CategoryService,
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

    this.getCategories();
  }

  createTableData(): void {
    this.artworkService.getAllArtworks().subscribe(
      (response: any) => {
        this.ngxService.stop();
        this.dataSource = new MatTableDataSource(response);
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

  handleAddAction() {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Add',
    };

    dialogConfig.width = '550px';
    const dialogRef = this.dialog.open(ArtworkComponent, dialogConfig);
    this.router.events.subscribe(() => {
      dialogRef.close();
    });

    const sub = dialogRef.componentInstance.onAddArtwork.subscribe(
      (response) => {
        this.createTableData();
      }
    );
  }

  handleDeleteAction(values: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      message: 'delete ' + values.title + ' artwork',
      confirmation: true,
    };
    const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe(
      (response) => {
        this.ngxService.start();
        this.deleteArtwork(values.id);
        dialogRef.close();
      }
    );
  }

  private deleteArtwork(id: any) {
    this.artworkService.deleteArtwork(id).subscribe(
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

  handleEditAction(data: any) {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Edit',
      data: data,
    };
    dialogConfig.width = '550px';
    const dialogRef = this.dialog.open(ArtworkComponent, dialogConfig);
    this.router.events.subscribe(() => {
      dialogRef.close();
    });
    const sub = dialogRef.componentInstance.onEditArtwork.subscribe(
      (response) => {
        this.createTableData();
      }
    );
  }

  filterArtworksByCategory(categoryId: number): void {
    if (categoryId === null) {
      this.createTableData();
      return;
    }

    this.artworkService.getArtworksByCategory(categoryId).subscribe(
      (artworks: Artwork[]) => {
        this.dataSource = new MatTableDataSource(artworks);
      },
      (error) => {
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

  private getCategories() {
    this.categoryService.getAllCategories().subscribe(
      (response: any) => {
        this.categories = response;
      },
      (error: any) => {
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
}
