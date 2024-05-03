import { Injectable } from '@angular/core';

export interface Menu {
  state: string;
  name: string;
  type: string;
  icon: string;
  role: string;
}

const MENUITEMS = [
  {
    state: 'dashboard',
    name: 'Dashboard',
    type: 'link',
    icon: 'dashboard',
    role: '',
  },
  {
    state: 'category',
    name: 'Category',
    type: 'link',
    icon: 'category',
    role: '',
  },
  {
    state: 'artwork',
    name: 'Artwork',
    type: 'link',
    icon: 'format_paint',
    role: '',
  },
  {
    state: 'user',
    name: 'Manage User',
    type: 'link',
    icon: 'people',
    role: 'admin',
  },
];
@Injectable()
export class MenuItems {
  getMenuitem(): Menu[] {
    return MENUITEMS;
  }
}
