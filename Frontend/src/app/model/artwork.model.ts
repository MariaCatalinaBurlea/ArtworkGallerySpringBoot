import { Category } from "./category.model";

export interface Artwork{
    id?: number;
    title: string;
    category?: Category;
    categoryId?: number;
    description: string;
    price: number;
    size: string;
    status: string; 
}