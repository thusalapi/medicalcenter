import React, { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "react-query";
import Link from "next/link";
import { reportTypeAPI } from "../../../utils/api";
import { ReportType } from "../../../types";

const ReportTemplatesPage: React.FC = () => {
  const queryClient = useQueryClient();
  const [error, setError] = useState<string | null>(null);

  // Fetch all report templates
  const { data: reportTypes, isLoading } = useQuery<ReportType[]>(
    "reportTypes",
    reportTypeAPI.getAllReportTypes,
    {
      onError: () => {
        setError("Failed to load report templates. Please try again later.");
      },
    }
  );

  // Delete report template mutation
  const deleteReportType = useMutation(
    (id: number) => reportTypeAPI.deleteReportType(id),
    {
      onSuccess: () => {
        // Invalidate and refetch the report types query
        queryClient.invalidateQueries("reportTypes");
      },
      onError: () => {
        setError(
          "Failed to delete report template. It may be in use by existing reports."
        );
      },
    }
  );

  const handleDelete = async (id: number, name: string) => {
    if (
      window.confirm(
        `Are you sure you want to delete the "${name}" report template?`
      )
    ) {
      await deleteReportType.mutateAsync(id);
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Report Templates</h1>
        <Link
          href="/admin/report-templates/new"
          className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
        >
          Create New Template
        </Link>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-800 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}

      {isLoading ? (
        <div className="text-center py-8">
          <div className="inline-block animate-spin rounded-full h-8 w-8 border-4 border-blue-500 border-t-transparent"></div>
          <p className="mt-2 text-gray-600">Loading templates...</p>
        </div>
      ) : !reportTypes?.length ? (
        <div className="bg-yellow-50 border border-yellow-200 text-yellow-800 p-4 rounded text-center">
          <p>
            No report templates found. Create your first template to get
            started!
          </p>
        </div>
      ) : (
        <div className="bg-white shadow overflow-hidden sm:rounded-md">
          <ul className="divide-y divide-gray-200">
            {reportTypes.map((reportType) => (
              <li key={reportType.reportTypeId}>
                <div className="px-4 py-5 sm:px-6 flex justify-between items-center">
                  <div>
                    <h3 className="text-lg font-medium text-gray-900">
                      {reportType.reportName}
                    </h3>
                    <p className="text-sm text-gray-500 mt-1">
                      {reportType.reportTemplate?.fields?.length || 0} field(s)
                    </p>
                  </div>
                  <div className="space-x-2">
                    <Link
                      href={`/admin/report-templates/${reportType.reportTypeId}`}
                      className="inline-block px-3 py-1 bg-blue-100 text-blue-700 rounded hover:bg-blue-200"
                    >
                      Edit
                    </Link>
                    <button
                      className="px-3 py-1 bg-red-100 text-red-700 rounded hover:bg-red-200"
                      onClick={() =>
                        handleDelete(
                          reportType.reportTypeId,
                          reportType.reportName
                        )
                      }
                      disabled={deleteReportType.isLoading}
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default ReportTemplatesPage;
