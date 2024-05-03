import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Artwork } from 'src/app/model/artwork.model';
import { Category } from 'src/app/model/category.model';
import { ArtworkService } from 'src/app/services/artwork.service';
import { CategoryService } from 'src/app/services/category.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-artwork',
  templateUrl: './artwork.component.html',
  styleUrls: ['./artwork.component.scss'],
})
export class ArtworkComponent implements OnInit {
  onAddArtwork = new EventEmitter();
  onEditArtwork = new EventEmitter();
  onDeleteArtwork = new EventEmitter();

  artworkForm: any = FormGroup;
  dialogAction: any = 'Add';
  action: any = 'Add';

  categories: Category[] = [];

  responseMessage: any;

  constructor(
    @Inject(MAT_DIALOG_DATA) public dialogData: any,
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<ArtworkComponent>,
    private snackbarService: SnackbarService,
    private artworkService: ArtworkService,
    private categoryService: CategoryService
  ) {}

  ngOnInit(): void {
    this.artworkForm = this.formBuilder.group({
      title: [null, [Validators.required]],
      categoryId: [null, [Validators.required]],
      description: [null, []],
      price: [null, []],
      size: [null, []],
      status: [null, []],
    });

    if (this.dialogData.action === 'Edit') {
      this.dialogAction = 'Edit';
      this.action = 'Update';
      this.artworkForm.patchValue(this.dialogData.data);
    } else {
      this.dialogAction = 'Add';
      this.action = 'Add';
    }

    this.getCategories();
  }

  handleSubmit(): void {
    if (this.dialogAction === 'Edit') {
      this.editArtwork();
    } else if (this.dialogAction === 'Add') {
      this.addArtwork();
    }
  }

  private addArtwork(): void {
    var formData = this.artworkForm.value;
    var data: Artwork = {
      categoryId: formData.categoryId,
      title: formData.title,
      price: formData.price,
      description: formData.description,
      size: formData.size,
      status: formData.status,
    };
    this.artworkService.addArtwork(data).subscribe(
      (response: any) => {
        this.dialogRef.close();
        this.onAddArtwork.emit();
        this.responseMessage = response.message;
        this.snackbarService.openSnackBar(this.responseMessage, 'success');
      },
      (error) => {
        this.dialogRef.close();
        console.error(error);
        if (error.error) {
          this.responseMessage = error.error;
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snackbarService.openSnackBar(this.responseMessage, 'error');
      }
    );
  }

  private editArtwork(): void {
    var formData = this.artworkForm.value;
    var data: Artwork = {
      id: this.dialogData.data.id,
      title: formData.title,
      categoryId: formData.categoryId,
      description: formData.description,
      price: formData.price,
      size: formData.size,
      status: formData.status,
    };
    this.artworkService.updateArtwork(data).subscribe(
      (response: any) => {
        this.dialogRef.close();
        this.onEditArtwork.emit();
        this.responseMessage = response.message;
        this.snackbarService.openSnackBar(this.responseMessage, 'success');
      },
      (error) => {
        this.dialogRef.close();
        console.error(error);
        if (error.error) {
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
