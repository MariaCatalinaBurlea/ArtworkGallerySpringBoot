import { Role } from "./enums/role.enum";

export interface User {
  id?: number;
  firstName: string;
  lastName: string;
  contactNumber?: string;
  password: string;
  email: string;
  address?: string;
  status?: string;
  role?: Role;
}
