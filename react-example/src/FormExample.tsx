import React, { useState } from "react";

interface Form {
  aaa: string;
  bbb: string;
  ccc: boolean;
  ddd: string;
  eee: string;
}

function defaultForm(): Form {
  return ({ aaa: "aaa", bbb: "bbb", ccc: true, ddd: "ddd2", eee: "eee2" });
}

export default function FormExample() {
  const [form, updateForm] = useForm(defaultForm);
  return (
    <div>
      <form>
        <p>aaa: <input type="text" name="aaa" value={form.aaa} onChange={updateForm}/></p>
        <p>bbb: <input type="password" name="bbb" value={form.bbb} onChange={updateForm}/></p>
        <p>ccc: <input type="checkbox" name="ccc" checked={form.ccc} onChange={updateForm}/></p>
        <p>ddd: {[1, 2, 3].map(a => (<input key={a} type="radio" name="ddd" value={`ddd${a}`} checked={form.ddd === `ddd${a}`} onChange={updateForm}/>))}</p>
        <p>eee: <select name="eee" value={form.eee} onChange={updateForm}>
          {[1, 2, 3].map(a => (<option key={a} value={`eee${a}`}>eee{a}</option>))}
        </select></p>

        <p>Formインターフェースにないプロパティxxxにも値を設定できてしまう。</p>
        <p>xxx: <input type="text" name="xxx" onChange={updateForm}/></p>
      </form>
      <hr/>
      <pre>{JSON.stringify(form, null, 4)}</pre>
    </div>
  );
}

function useForm<T>(defaultForm: () => T): [T, React.ChangeEventHandler<HTMLInputElement|HTMLSelectElement>] {
  const [form, setForm] = useState(defaultForm);
  const updateForm: React.ChangeEventHandler<HTMLInputElement|HTMLSelectElement> = event => {
    const { target } = event;
    const name = target.name;
    const value = (target instanceof HTMLInputElement && target.type === "checkbox") ? target.checked : target.value;
    setForm(form => ({ ...form, [name]: value }));
  };
  return [form, updateForm];
}

