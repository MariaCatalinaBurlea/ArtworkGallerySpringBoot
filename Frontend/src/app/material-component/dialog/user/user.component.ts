import { Component, EventEmitter, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Role } from 'src/app/model/enums/role.enum';
import { User } from 'src/app/model/user.model';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { UserService } from 'src/app/services/user.service';
import { GlobalConstants } from 'src/app/shared/global-constants';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
})
export class UserComponent implements OnInit {
  onEditUser = new EventEmitter();

  userForm: any = FormGroup;

  responseMessage: any;
  
  roles = Object.values(Role);
  
  constructor(
    @Inject(MAT_DIALOG_DATA) public dialogData: any,
    private formBulider: FormBuilder,
    protected userService: UserService,
    public dialogRef: MatDialogRef<UserComponent>,
    private snackbarService: SnackbarService
  ) {}

  ngOnInit(): void {
    this.userForm = this.formBulider.group({
      firstName: [null, [Validators.required]],
      lastName: [null, [Validators.required]],
      contactNumber: [null, []],
      email: [null, [Validators.required]],
      address: [null, []],
      status: [null, []],
      role: [null, []]
    });

    this.userForm.patchValue(this.dialogData.data);
  }

  handleEditUser(): void{
    var formData = this.userForm.value;
    var data = {
      id: this.dialogData.data.id,
      firstName: formData.firstName,
      lastName: formData.lastName,
      email: formData.email,
      contactNumber: formData.contactNumber,
      status: formData.status,
      role: formData.role
    };

    this.userService.updateUser(data).subscribe(
      (response: any) => {
        this.dialogRef.close();
        this.onEditUser.emit();
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
}
