import { Routes } from "@angular/router";
import { CreateJob } from "./create-job/create-job";
import { JobsListComponent } from "./jobs-list/jobs-list"; // <-- Importando a listagem
import { roleGuard } from "../../core/services/role.guard";
import { authGuard } from "../../core/services/auth.guard";

export const jobsRoutes: Routes = [
    {
        path: '', // Rota principal ('/jobs') lista as vagas
        component: JobsListComponent
    },
    {
        path: 'create', // Sua rota atual ('/jobs/create')
        component: CreateJob,
        canActivate: [authGuard, roleGuard],
        data: {
            role: 'company'
        }
    }
];