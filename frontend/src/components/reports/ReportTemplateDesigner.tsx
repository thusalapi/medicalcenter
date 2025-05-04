import React, { useState, useEffect } from "react";
import {
  DndContext,
  closestCenter,
  KeyboardSensor,
  PointerSensor,
  useSensor,
  useSensors,
  DragEndEvent,
} from "@dnd-kit/core";
import {
  arrayMove,
  SortableContext,
  sortableKeyboardCoordinates,
  verticalListSortingStrategy,
} from "@dnd-kit/sortable";
import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import { ReportField } from "../../types";

interface Props {
  fields: ReportField[];
  onChange: (fields: ReportField[]) => void;
  onSave: () => void;
}

// Single field component (draggable)
const SortableField = ({
  field,
  onUpdate,
}: {
  field: ReportField;
  onUpdate: (field: ReportField) => void;
}) => {
  const { attributes, listeners, setNodeRef, transform, transition } =
    useSortable({ id: field.id });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
    marginBottom: "10px",
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    onUpdate({ ...field, [name]: value });
  };

  return (
    <div
      ref={setNodeRef}
      style={style}
      className="bg-white p-4 border rounded-md shadow-sm"
    >
      <div className="flex items-center justify-between mb-2">
        <div
          className="cursor-move bg-gray-200 px-2 py-1 rounded text-sm"
          {...attributes}
          {...listeners}
        >
          Drag
        </div>
        <div className="text-sm text-gray-500">ID: {field.id}</div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Label
          </label>
          <input
            type="text"
            name="label"
            value={field.label}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-md"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Field Type
          </label>
          <select
            name="type"
            value={field.type}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-md"
          >
            <option value="text">Text</option>
            <option value="number">Number</option>
            <option value="date">Date</option>
            <option value="checkbox">Checkbox</option>
          </select>
        </div>
      </div>

      <div className="mt-3 grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            X Position (optional)
          </label>
          <input
            type="number"
            name="x"
            value={field.x || ""}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-md"
            placeholder="Auto"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Y Position (optional)
          </label>
          <input
            type="number"
            name="y"
            value={field.y || ""}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-md"
            placeholder="Auto"
          />
        </div>
      </div>

      <div className="mt-3 grid grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Font Size
          </label>
          <input
            type="number"
            name="fontSize"
            value={field.fontSize || 12}
            onChange={handleChange}
            className="w-full px-3 py-2 border border-gray-300 rounded-md"
          />
        </div>

        <div className="flex items-center mt-6">
          <input
            type="checkbox"
            name="bold"
            checked={field.bold || false}
            onChange={(e) => onUpdate({ ...field, bold: e.target.checked })}
            className="mr-2"
          />
          <label className="text-sm font-medium text-gray-700">Bold</label>

          <input
            type="checkbox"
            name="showLabel"
            checked={field.showLabel || false}
            onChange={(e) =>
              onUpdate({ ...field, showLabel: e.target.checked })
            }
            className="ml-4 mr-2"
          />
          <label className="text-sm font-medium text-gray-700">
            Show Label
          </label>
        </div>
      </div>
    </div>
  );
};

// Main report template designer component
const ReportTemplateDesigner: React.FC<Props> = ({
  fields,
  onChange,
  onSave,
}) => {
  const [items, setItems] = useState<ReportField[]>(fields);

  // Update parent when items change
  useEffect(() => {
    onChange(items);
  }, [items, onChange]);

  const sensors = useSensors(
    useSensor(PointerSensor),
    useSensor(KeyboardSensor, {
      coordinateGetter: sortableKeyboardCoordinates,
    })
  );

  const handleDragEnd = (event: DragEndEvent) => {
    const { active, over } = event;

    if (over && active.id !== over.id) {
      setItems((items) => {
        const oldIndex = items.findIndex((item) => item.id === active.id);
        const newIndex = items.findIndex((item) => item.id === over.id);

        return arrayMove(items, oldIndex, newIndex);
      });
    }
  };

  const updateField = (updatedField: ReportField) => {
    setItems((prevItems) =>
      prevItems.map((field) =>
        field.id === updatedField.id ? updatedField : field
      )
    );
  };

  const addNewField = () => {
    const newId = `field_${Date.now()}`;
    const newField: ReportField = {
      id: newId,
      label: `Field ${items.length + 1}`,
      type: "text",
      fontSize: 12,
      showLabel: true,
    };

    setItems([...items, newField]);
  };

  return (
    <div className="bg-gray-50 p-6 border rounded-lg">
      <div className="flex justify-between items-center mb-6">
        <h3 className="text-lg font-medium">Report Template Designer</h3>
        <div>
          <button
            type="button"
            onClick={addNewField}
            className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 mr-2"
          >
            Add Field
          </button>
          <button
            type="button"
            onClick={onSave}
            className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
          >
            Save Template
          </button>
        </div>
      </div>

      <div className="bg-gray-100 border-2 border-dashed border-gray-300 rounded-lg p-4 min-h-[300px]">
        <DndContext
          sensors={sensors}
          collisionDetection={closestCenter}
          onDragEnd={handleDragEnd}
        >
          <SortableContext
            items={items.map((field) => field.id)}
            strategy={verticalListSortingStrategy}
          >
            {items.map((field) => (
              <SortableField
                key={field.id}
                field={field}
                onUpdate={updateField}
              />
            ))}
          </SortableContext>
        </DndContext>

        {items.length === 0 && (
          <div className="text-center py-10 text-gray-500">
            <p>
              No fields added yet. Click &quot;Add Field&quot; to start
              designing your template.
            </p>
          </div>
        )}
      </div>

      <div className="mt-4 flex justify-end">
        <button
          type="button"
          onClick={() => setItems([])}
          className="px-3 py-1 text-sm text-red-600 hover:text-red-800"
        >
          Clear All Fields
        </button>
      </div>
    </div>
  );
};

export default ReportTemplateDesigner;
