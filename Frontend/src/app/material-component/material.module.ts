import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CdkTableModule } from '@angular/cdk/table';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FlexLayoutModule } from '@angular/flex-layout';

import { MaterialRoutes } from './material.routing';
import { MaterialModule } from '../shared/material-module';
import { ViewBillProductsComponent } from './dialog/view-bill-products/view-bill-products.component';
import { ManageCategoryComponent } from './manage-category/manage-category.component';
import { CategoryComponent } from './dialog/category/category.component';
import { ConfirmationComponent } from './dialog/confirmation/confirmation.component';
import { ManageArtworkComponent } from './manage-artwork/manage-artwork.component';
import { ArtworkComponent } from './dialog/artwork/artwork.component';
import { ManageUserComponent } from './manage-user/manage-user.component';
import { UserComponent } from './dialog/user/user.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(MaterialRoutes),
    MaterialModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    FlexLayoutModule,
    CdkTableModule
  ],
  providers: [],
  declarations: [
    ViewBillProductsComponent,
    ManageCategoryComponent,
    CategoryComponent,
    ConfirmationComponent,
    ManageArtworkComponent,
    ArtworkComponent,
    ManageUserComponent,
    UserComponent,
  ]
})
export class MaterialComponentsModule {}
