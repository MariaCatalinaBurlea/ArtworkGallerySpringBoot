import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { NgxUiLoaderService } from 'ngx-ui-loader';
import { Category } from 'src/app/model/category.model';
import { User } from 'src/app/model/user.model';
import { AuthService } from 'src/app/services/auth.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { UserService } from 'src/app/services/user.service';
import { GlobalConstants } from 'src/app/shared/global-constants';
import { ConfirmationComponent } from '../dialog/confirmation/confirmation.component';
import { Role } from 'src/app/model/enums/role.enum';
import { UserComponent } from '../dialog/user/user.component';

@Component({
  selector: 'app-manage-user',
  templateUrl: './manage-user.component.html',
  styleUrls: ['./manage-user.component.scss'],
})
export class ManageUserComponent implements OnInit {
  displayedColumns: string[] = [
    'id',
    'firstName',
    'lastName',
    'email',
    'contactNumber',
    'address',
    'role',
    'status',
    'edit',
    'delete'
  ];

  displayedColumnsForEmails: string[] = ['email'];
  activateEmailsTable: boolean = false;

  dataSource: any;
  dataSourceForEmails: any;
  responseMessage: any;

  roles = Object.values(Role);

  constructor(
    private userService: UserService,
    private ngxService: NgxUiLoaderService,
    private dialog: MatDialog,
    private snackbarService: SnackbarService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.ngxService.start();
    this.createTableData();
  }

  createTableData(): void {
    this.userService.getAll().subscribe(
      (users: User[]) => {
        this.ngxService.stop();
        console.log(users);
        this.dataSource = new MatTableDataSource(users);
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
    console.log('edit pressed');
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      action: 'Edit',
      data: data,
    };
    dialogConfig.width = '550px';
    const dialogRef = this.dialog.open(UserComponent, dialogConfig);
    this.router.events.subscribe(() => {
      dialogRef.close();
    });
    const sub = dialogRef.componentInstance.onEditUser.subscribe(
      (response) => {
        this.createTableData();
      }
    );
  }

  handleDeleteAction(values: any) {
    console.log('delete pressed');
    const dialogConfig = new MatDialogConfig();
    dialogConfig.data = {
      message: 'delete ' + values.email + ' user',
      confirmation: true,
    };
    const dialogRef = this.dialog.open(ConfirmationComponent, dialogConfig);
    const sub = dialogRef.componentInstance.onEmitStatusChange.subscribe(
      (response) => {
        this.ngxService.start();
        this.deleteUser(values.id);
        dialogRef.close();
      }
    );
  }

  onChange(status: any, id: any) {
    this.ngxService.start();
    var data = {
      status: status.toString(),
      id: id,
    };
    this.userService.updateStatus(data).subscribe(
      (response: any) => {
        this.ngxService.stop();
        this.responseMessage = response?.message;
        this.snackbarService.openSnackBar(this.responseMessage, 'Succes');
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

  private deleteUser(id: any) {
    this.userService.deleteUser(id).subscribe(
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

  filterByRole(role: string){
    if(role === null){
      this.createTableData();
      return;
    }

    this.userService.getByRole(role).subscribe(
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


  getUsers(){
    this.userService.getUsers().subscribe(
      (response: any) => {
        this.ngxService.stop();
        console.log(response);
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

  getAdminsEmails(){
    this.activateEmailsTable = !this.activateEmailsTable;

    this.userService.getAdminsEmails().subscribe(
      (response: any) => {
        this.ngxService.stop();
        console.log(response);
        this.dataSourceForEmails = new MatTableDataSource(response);
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
}
