import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { SnackbarService } from '../services/snackbar.service';
import { UserService } from '../services/user.service';
import { SignupComponent } from '../signup/signup.component';
import { GlobalConstants } from '../shared/global-constants';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  hide: boolean = true;
  loginForm: any = FormGroup;
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
    this.loginForm = this.formBuilder.group({
      email: [null, [Validators.required]],
      password: [null, [Validators.required]],
    });
  }

  toggleLogin() {
    this.ngxService.start();
    let formData = this.loginForm.value;
    let data = {
      email: formData.email,
      password: formData.password
    };

    this.userService.login(data).subscribe(
      (response: any) => {
        this.ngxService.stop();
        this.dialogRef.close();
        localStorage.setItem('token', response.token);
        this.router.navigate(['/artgallery/dashboard']);
      },
      (error) => {
        this.ngxService.stop();
        if (error.error) {
          console.log(error.error);
          this.responseMessage = error.error;
        } else {
          this.responseMessage = GlobalConstants.genericError;
        }
        this.snackbarService.openSnackBar(this.responseMessage, "error");
      }
    );
  }
}
