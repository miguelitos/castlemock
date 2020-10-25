/*
 Copyright 2020 Karl Dahlgren

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

import React, {PureComponent} from 'react';
import {Link} from "react-router-dom";
import axios from "axios";
import ToolkitProvider, {Search} from "react-bootstrap-table2-toolkit";
import BootstrapTable from "react-bootstrap-table-next";
import PaginationFactory from "react-bootstrap-table2-paginator";
import validateErrorResponse from "../../../../utility/HttpResponseValidator";
import Badge from "react-bootstrap/Badge";
import UpdateStatusModal from "./modal/UpdateStatusModal";
import UpdateProjectModal from "./modal/UpdateProjectModal"
import UpdateEndpointModal from "./modal/UpdateEndpointModal";
import DeleteProjectModal from "./modal/DeleteProjectModal";
import DeleteApplicationsModal from "./modal/DeleteApplicationsModal";
import CreateApplicationModal from "./modal/CreateApplicationModal";

const { SearchBar } = Search;
const SELECT = true;
const DESELECT = false;

class RestProject extends PureComponent {

    constructor(props) {
        super(props);

        this.onRowSelect = this.onRowSelect.bind(this);
        this.onRowSelectAll = this.onRowSelectAll.bind(this);
        this.nameFormat = this.nameFormat.bind(this);
        this.onExportProjectClick = this.onExportProjectClick.bind(this);
        this.getProject = this.getProject.bind(this);

        this.columns = [{
            dataField: 'id',
            text: 'id',
            hidden: true
        }, {
            dataField: 'name',
            text: 'Name',
            sort: true,
            formatter: this.nameFormat
        }, {
            dataField: 'statusCount.MOCKED',
            text: 'Mocked',
            sort: true
        }, {
            dataField: 'statusCount.DISABLED',
            text: 'Disabled',
            sort: true
        }, {
            dataField: 'statusCount.FORWARDED',
            text: 'Forwarded',
            sort: true
        }, {
            dataField: 'statusCount.RECORDING',
            text: 'Recording',
            sort: true
        }, {
            dataField: 'statusCount.RECORD_ONCE',
            text: 'Record once',
            sort: true
        }, {
            dataField: 'statusCount.ECHO',
            text: 'Echo',
            sort: true
        }];

        this.selectRow = {
            mode: 'checkbox',
            onSelect: this.onRowSelect,
            onSelectAll: this.onRowSelectAll
        };

        this.defaultSort = [{
            dataField: 'name',
            order: 'asc'
        }];

        this.state = {
            projectId: this.props.match.params.projectId,
            project: {
                applications: []
            },
            selectedApplications: []
        };

        this.getProject();
    }

    onRowSelect(value, mode) {
        let applications = this.state.selectedApplications.slice();
        let application = {
            id: value.id,
            name: value.name
        };
        if(mode === SELECT){
            applications.push(application);
        } else if(mode === DESELECT){
            let index = applications.indexOf(application);
            applications.splice(index, 1);
        }
        this.setState({
            selectedApplications: applications
        });
    }

    onRowSelectAll(mode) {
        if(mode === SELECT){
            let applications = [];
            this.state.project.applications.forEach(value => {
                let application = {
                    id: value.id,
                    name: value.name
                };
                applications.push(application);
            });
            this.setState({
                selectedApplications: applications
            });
        } else if(mode === DESELECT){
            this.setState({
                selectedApplications: []
            });
        }
    }

    nameFormat(cell, row) {
        if(cell == null){
            return;
        }

        return (
            <div className="table-link">
                <Link to={"/web/rest/project/" + this.state.projectId + "/application/" + row.id}>{cell}</Link>
            </div>
        )
    }

    getProject() {
        axios
            .get("/api/rest/rest/project/" + this.state.projectId)
            .then(response => {
                this.setState({
                    project: response.data
                });
            })
            .catch(error => {
                validateErrorResponse(error)
            });
    }

    onExportProjectClick() {
        axios({
            url:  "/api/rest/core/project/rest/" + this.state.projectId + "/export",
            method: 'GET',
            responseType: 'blob'
        }).then((response) => {
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', this.state.projectId + ".xml");
            document.body.appendChild(link);
            link.click();
        })
            .catch(error => {
                validateErrorResponse(error)
            });
    }



    render() {
        return (
            <div>
                <section>
                    <div className="navigation">
                        <nav aria-label="breadcrumb">
                            <ol className="breadcrumb breadcrumb-custom">
                                <li className="breadcrumb-item"><Link to={"/web"}>Home</Link></li>
                                <li className="breadcrumb-item">{this.state.project.name}</li>
                            </ol>
                        </nav>
                    </div>
                    <div className="content-top">
                        <div className="title">
                            <h1>Project: {this.state.project.name}</h1>
                        </div>
                        <div className="menu">
                            <button className="btn btn-success demo-button-disabled menu-button" data-toggle="modal" data-target="#updateProjectModal"><span>Update project</span></button>
                            <button className="btn btn-primary demo-button-disabled menu-button" data-toggle="modal" data-target="#createApplicationModal"><span>Create application</span></button>
                            <div className="btn-group demo-button-disabled menu-button" role="group">
                                <button id="btnGroupDrop1" type="button" className="btn btn-primary dropdown-toggle"
                                        data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                    Upload
                                </button>
                                <div className="dropdown-menu" aria-labelledby="btnGroupDrop1">
                                    <button className="dropdown-item" data-toggle="modal" data-target="#uploadRAMLModal">RAML</button>
                                    <button className="dropdown-item" data-toggle="modal" data-target="#uploadSwaggerModal">Swagger</button>
                                    <button className="dropdown-item" data-toggle="modal" data-target="#uploadWADLModal">WADL</button>
                                </div>
                            </div>                            <button className="btn btn-primary demo-button-disabled menu-button" onClick={this.onExportProjectClick}><span>Export project</span></button>
                            <button className="btn btn-danger demo-button-disabled menu-button" data-toggle="modal" data-target="#deleteProjectModal"><span>Delete project</span></button>
                        </div>
                    </div>
                    <div className="content-summary">
                        <dl className="row">
                            <dt className="col-sm-2 content-title content-title">Description</dt>
                            <dd className="col-sm-9">{this.state.project.description}</dd>
                        </dl>
                        <dl className="row">
                            <dt className="col-sm-2 content-title">Type</dt>
                            <dd className="col-sm-9"><Badge variant="success">REST</Badge></dd>
                        </dl>
                    </div>
                    <div className="panel panel-primary table-panel">
                        <div className="panel-heading table-panel-heading">
                            <h3 className="panel-title">Applications</h3>
                        </div>
                        <div className="table-result">
                            <ToolkitProvider bootstrap4
                                             columns={ this.columns}
                                             data={this.state.project.applications}
                                             keyField="id"
                                             search>
                                {
                                    (props) => (
                                        <div>
                                            <div>
                                                <SearchBar {...props.searchProps} className={"table-filter-field"}/>
                                            </div>
                                            <BootstrapTable {...props.baseProps} bootstrap4
                                                            data={this.state.project.applications} columns={this.columns}
                                                            defaultSorted={this.defaultSort} keyField='id' hover
                                                            selectRow={this.selectRow}
                                                            noDataIndication="Click on the 'Upload' to upload a REST API definition"
                                                            pagination={ PaginationFactory() }/>
                                        </div>
                                    )}
                            </ToolkitProvider>
                            <div className="panel-buttons">
                                <button className="btn btn-primary demo-button-disabled menu-button" data-toggle="modal"
                                        disabled={this.state.selectedApplications.length === 0}
                                        data-target="#updateStatusModal"><span>Update status</span></button>
                                <button className="btn btn-primary demo-button-disabled menu-button" data-toggle="modal"
                                        disabled={this.state.selectedApplications.length === 0}
                                        data-target="#updateEndpointModal"><span>Update endpoint</span></button>
                                <button className="btn btn-danger demo-button-disabled menu-button" data-toggle="modal"
                                        disabled={this.state.selectedApplications.length === 0}
                                        data-target="#deleteApplicationsModal"><span>Delete application</span></button>
                            </div>
                        </div>
                    </div>
                </section>

                <CreateApplicationModal projectId={this.state.projectId}/>
                <DeleteApplicationsModal projectId={this.state.projectId} selectedApplications={this.state.selectedApplications} getProject={this.getProject}/>
                <DeleteProjectModal projectId={this.state.projectId}/>
                <UpdateEndpointModal projectId={this.state.projectId} getProject={this.getProject} selectedApplications={this.state.selectedApplications}/>
                <UpdateProjectModal projectId={this.state.projectId} getProject={this.getProject} project={this.state.project}/>
                <UpdateStatusModal projectId={this.state.projectId} getProject={this.getProject} selectedApplications={this.state.selectedApplications}/>
            </div>
        )
    }

}

export default RestProject;