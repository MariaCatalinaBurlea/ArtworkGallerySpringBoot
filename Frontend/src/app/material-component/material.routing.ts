import { Routes } from '@angular/router';
import { ManageCategoryComponent } from './manage-category/manage-category.component';
import { RouteGuardService } from '../services/route-guard.service';
import { ManageArtworkComponent } from './manage-artwork/manage-artwork.component';
import { ManageUserComponent } from './manage-user/manage-user.component';


export const MaterialRoutes: Routes = [
    {
        path: 'category',
        component: ManageCategoryComponent,
        canActivate: [RouteGuardService],
        data: {
            expectedRole:  ['user', 'admin']
        }
    },
    {
        path: 'artwork',
        component: ManageArtworkComponent,
        canActivate: [RouteGuardService],
        data: {
            expectedRole:  ['user', 'admin']
        }
    },
    {
        path: 'user',
        component: ManageUserComponent,
        canActivate: [RouteGuardService],
        data: {
            expectedRole:  ['admin']
        }
    },
];
