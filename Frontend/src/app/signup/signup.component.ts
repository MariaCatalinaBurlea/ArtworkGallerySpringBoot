import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { SnackbarService } from '../services/snackbar.service';
import { MatDialogRef } from '@angular/material/dialog';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { GlobalConstants } from '../shared/global-constants';
import { User } from '../model/user.model';
import { Role } from '../model/enums/role.enum';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
})
export class SignupComponent implements OnInit {
  password: boolean = true;
  signupForm: any = FormGroup;
  responseMessage: any;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private userService: UserService,
    private snackbarService: SnackbarService,
    public dialogRef: MatDialogRef<SignupComponent>,
    private ngxService: NgxUiLoaderService
  ) {}

  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      firstName: [null, [Validators.required]],
      lastName: [null, [Validators.required]],
      contactNumber: [null, []],
      email: [null, [Validators.required]],
      password: [null, [Validators.required]],
      address: [null, []],
    });
  }

  toggleSubmit() {
    this.ngxService.start();
    let formData = this.signupForm.value;
    const user: User = {
      firstName: formData.firstName,
      lastName: formData.lastName,
      contactNumber: formData.contactNumber,
      email: formData.email,
      password: formData.password,
      address: formData.address,
      role: Role.USER, // Assign the role here
    };


    this.userService.signup(user).subscribe(
      (response: any) => {
        this.ngxService.stop();
        this.dialogRef.close();
        this.responseMessage = response?.message;
        this.snackbarService.openSnackBar(this.responseMessage, '');
        this.router.navigate(['/']);
      },
      (error) => {
        this.ngxService.stop();
        if (error.error) {
          this.responseMessage = this.getErrorMessage(error.error);
          this.snackbarService.openSnackBar(this.responseMessage, "error");
        } else {
          this.responseMessage = GlobalConstants.genericError;
          this.snackbarService.openSnackBar(this.responseMessage, "");
        }
      }
    );
  }

  private getErrorMessage(error: any): string {
    // Removing {"message": from the error message
    let errorMessage = error.replace('{"message":', '').slice(0, -1);

    // Remove content between -> and :
    errorMessage = errorMessage.replace(/->.*?:/g, '');

    // Remove -> and :
    errorMessage = errorMessage.replace(/->|:/g, '');

    // Adding newline before each arrow
    errorMessage = errorMessage.replace(/->/g, '\n->');

    return errorMessage;
  }
}
